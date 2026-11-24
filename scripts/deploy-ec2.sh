#!/usr/bin/env bash
set -euo pipefail

usage() {
    cat <<'USAGE'
Deploy the Visitor rules engine to an EC2 Linux instance over SSH.

Usage:
  scripts/deploy-ec2.sh --host HOST --user USER [options]

Options:
  --key PATH          SSH private key (omit to use your SSH agent)
  --branch NAME       Git branch to deploy (default: work)
  --remote-dir PATH   Absolute deployment directory (default: ~/visitor-rules-engine-deploy)
  --ssh-port PORT     SSH port (default: 22)
  --http-port PORT    Jetty HTTP port (default: 8080)
  --help              Show this help

The instance needs Git, Java, Maven, curl, flock, systemd, and passwordless sudo
for the SSH user. The script installs a visitor-rules-engine systemd service.
USAGE
}

fail() {
    printf 'deploy-ec2: %s\n' "$*" >&2
    exit 1
}

deploy_host=''
deploy_user=''
deploy_key=''
deploy_branch='work'
deploy_remote_dir='-'
deploy_ssh_port='22'
deploy_http_port='8080'

while (($#)); do
    case "$1" in
        --host|--user|--key|--branch|--remote-dir|--ssh-port|--http-port)
            (($# >= 2)) || fail "missing value for $1"
            case "$1" in
                --host) deploy_host=$2 ;;
                --user) deploy_user=$2 ;;
                --key) deploy_key=$2 ;;
                --branch) deploy_branch=$2 ;;
                --remote-dir) deploy_remote_dir=$2 ;;
                --ssh-port) deploy_ssh_port=$2 ;;
                --http-port) deploy_http_port=$2 ;;
            esac
            shift 2
            ;;
        --help|-h)
            usage
            exit 0
            ;;
        *) fail "unknown option: $1" ;;
    esac
done

[[ "$deploy_host" =~ ^[A-Za-z0-9][A-Za-z0-9.-]*$ ]] || fail 'provide an EC2 public DNS name or IPv4 address with --host'
[[ "$deploy_user" =~ ^[A-Za-z_][A-Za-z0-9_-]*$ ]] || fail 'provide the instance login name with --user'
[[ "$deploy_branch" =~ ^[A-Za-z0-9][A-Za-z0-9._/-]*$ ]] || fail 'invalid branch name'
git check-ref-format --branch "$deploy_branch" >/dev/null || fail 'invalid Git branch name'
if [[ "$deploy_remote_dir" != '-' ]]; then
    [[ "$deploy_remote_dir" =~ ^/[A-Za-z0-9_./-]+$ ]] || fail '--remote-dir must be an absolute path without spaces'
fi
for deploy_port in "$deploy_ssh_port" "$deploy_http_port"; do
    [[ "$deploy_port" =~ ^[0-9]+$ ]] || fail 'ports must be numbers'
    ((10#$deploy_port >= 1 && 10#$deploy_port <= 65535)) || fail 'ports must be between 1 and 65535'
done
if [[ -n "$deploy_key" ]]; then
    [[ -r "$deploy_key" ]] || fail "SSH key is not readable: $deploy_key"
fi

deploy_ssh_args=(-o BatchMode=yes -o StrictHostKeyChecking=accept-new -o ConnectTimeout=15 -p "$deploy_ssh_port")
if [[ -n "$deploy_key" ]]; then
    deploy_ssh_args+=(-i "$deploy_key" -o IdentitiesOnly=yes)
fi
printf -v deploy_remote_command 'bash -s -- %q %q %q' "$deploy_branch" "$deploy_remote_dir" "$deploy_http_port"
ssh "${deploy_ssh_args[@]}" "$deploy_user@$deploy_host" "$deploy_remote_command" <<'REMOTE_SCRIPT'
set -euo pipefail

remote_branch=$1
remote_base=$2
remote_http_port=$3
remote_repo_url='https://github.com/pseudohuman92/visitor-rules-engine.git'
remote_service='visitor-rules-engine.service'
if [[ "$remote_base" == '-' ]]; then
    remote_base="$HOME/visitor-rules-engine-deploy"
fi

remote_fail() {
    printf 'deploy-ec2 (instance): %s\n' "$*" >&2
    exit 1
}

for remote_command in git java mvn curl flock systemctl sudo tar; do
    command -v "$remote_command" >/dev/null || remote_fail "missing required command: $remote_command"
done
sudo -n true 2>/dev/null || remote_fail 'the SSH user needs passwordless sudo to manage the systemd service'
remote_mvn=$(command -v mvn)
remote_user=$(id -un)
[[ "$remote_mvn" =~ ^/[A-Za-z0-9_./-]+$ ]] || remote_fail 'Maven must resolve to an absolute path without spaces'

mkdir -p "$remote_base/releases"
exec 9>"$remote_base/deploy.lock"
flock -n 9 || remote_fail 'another deployment is already running'
remote_source="$remote_base/source"
if [[ ! -e "$remote_source" ]]; then
    git clone --branch "$remote_branch" "$remote_repo_url" "$remote_source"
fi
[[ -d "$remote_source/.git" ]] || remote_fail "not a Git checkout: $remote_source"
case "$(git -C "$remote_source" remote get-url origin)" in
    "$remote_repo_url"|git@github.com:pseudohuman92/visitor-rules-engine.git) ;;
    *) remote_fail "unexpected origin URL in $remote_source" ;;
esac
[[ -z "$(git -C "$remote_source" status --porcelain)" ]] || remote_fail "source checkout has local changes: $remote_source"
git -C "$remote_source" fetch --prune origin "$remote_branch"
if git -C "$remote_source" show-ref --verify --quiet "refs/heads/$remote_branch"; then
    git -C "$remote_source" switch "$remote_branch"
else
    git -C "$remote_source" switch --track -c "$remote_branch" "origin/$remote_branch"
fi
git -C "$remote_source" pull --ff-only origin "$remote_branch"
git -C "$remote_source" submodule update --init --recursive
remote_commit=$(git -C "$remote_source" rev-parse HEAD)
[[ "$remote_commit" == "$(git -C "$remote_source" rev-parse "origin/$remote_branch")" ]] || remote_fail 'local branch has unpublished commits'
remote_release="$remote_base/releases/$remote_commit"

remote_staging=''
remote_next_link="$remote_base/current.next.$$"
remote_cleanup() {
    if [[ -n "$remote_staging" && -d "$remote_staging" ]]; then
        rm -rf -- "$remote_staging"
    fi
    if [[ -L "$remote_next_link" ]]; then
        rm -f -- "$remote_next_link"
    fi
}
trap remote_cleanup EXIT
if [[ ! -f "$remote_release/.built" ]]; then
    [[ ! -e "$remote_release" ]] || remote_fail "incomplete release already exists: $remote_release"
    remote_staging=$(mktemp -d "$remote_base/releases/.build.XXXXXXXX")
    git -C "$remote_source" archive "$remote_commit" | tar -x -C "$remote_staging"
    mkdir -p "$remote_staging/src/main/proto"
    git -C "$remote_source/src/main/proto" archive HEAD | tar -x -C "$remote_staging/src/main/proto"
    (cd "$remote_staging" && mvn -B -ntp verify)
    touch "$remote_staging/.built"
    mv -- "$remote_staging" "$remote_release"
    remote_staging=''
fi

remote_current="$remote_base/current"
[[ ! -e "$remote_current" || -L "$remote_current" ]] || remote_fail "$remote_current must be a symlink"
remote_previous=''
if [[ -L "$remote_current" ]]; then
    remote_previous=$(readlink "$remote_current")
fi
remote_unit='/etc/systemd/system/visitor-rules-engine.service'
if sudo -n test -e "$remote_unit" && ! sudo -n grep -q '^# Managed by scripts/deploy-ec2.sh$' "$remote_unit"; then
    remote_fail "existing $remote_unit is not managed by this script"
fi
remote_unit_tmp=$(mktemp)
cat >"$remote_unit_tmp" <<UNIT
# Managed by scripts/deploy-ec2.sh
[Unit]
Description=Visitor rules engine
Wants=network-online.target
After=network-online.target

[Service]
Type=simple
User=$remote_user
WorkingDirectory=$remote_current
ExecStart=$remote_mvn -B -ntp -DskipTests -Djetty.http.port=$remote_http_port jetty:run
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
UNIT
sudo -n install -m 0644 "$remote_unit_tmp" "$remote_unit"
rm -f "$remote_unit_tmp"
sudo -n systemctl daemon-reload
sudo -n systemctl enable "$remote_service" >/dev/null

ln -s "$remote_release" "$remote_next_link"
mv -Tf "$remote_next_link" "$remote_current"

remote_ready() {
    for ((remote_attempt = 0; remote_attempt < 30; remote_attempt++)); do
        if sudo -n systemctl is-active --quiet "$remote_service" && curl -fsS --max-time 3 "http://127.0.0.1:$remote_http_port/" >/dev/null; then
            return 0
        fi
        sleep 2
    done
    return 1
}

if ! sudo -n systemctl restart "$remote_service" || ! remote_ready; then
    printf 'New release did not become healthy; restoring previous release.\n' >&2
    if [[ -n "$remote_previous" && "$remote_previous" != "$remote_release" ]]; then
        ln -s "$remote_previous" "$remote_next_link"
        mv -Tf "$remote_next_link" "$remote_current"
        sudo -n systemctl restart "$remote_service" || true
    else
        sudo -n systemctl stop "$remote_service" || true
        if [[ -z "$remote_previous" ]]; then
            rm -f -- "$remote_current"
        fi
    fi
    sudo -n journalctl -u "$remote_service" -n 60 --no-pager >&2 || true
    remote_fail "server did not respond on port $remote_http_port"
fi

printf 'Deployed %s (%s) to %s\n' "$remote_commit" "$remote_branch" "$remote_current"
printf 'Server ready at http://127.0.0.1:%s/ on the instance\n' "$remote_http_port"
REMOTE_SCRIPT
