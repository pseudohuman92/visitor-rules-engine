# Visitor: the Card Game

## To run the server

### Required Software

Install Maven: https://maven.apache.org/

Install Protobuf (protoc, protobuf-java, protobuf-js): https://github.com/protocolbuffers/protobuf/releases

---

To build: mvn install

To start: mvn jetty:run

To stop: <CTRL+C> or mvn jetty:stop (if running in background)

### Deploy to an EC2 instance

From a machine that can SSH to the instance, run:

```sh
scripts/deploy-ec2.sh --host ec2-example.compute.amazonaws.com --user ubuntu --key ~/.ssh/visitor.pem
```

The script pulls the latest `work` branch and its protobuf submodule, runs
`mvn verify` in a new release directory, and restarts a `visitor-rules-engine`
systemd service. It checks
`http://127.0.0.1:8080/` on the instance and restores the previous release if
the new one does not start. Use `--branch master` to deploy `master`, or
`--http-port PORT` to change the HTTP port. Run `scripts/deploy-ec2.sh --help`
for all options. The instance needs Git, Java, Maven, curl, flock, systemd, and
passwordless sudo for the SSH user. Configure the EC2 security group and any
TLS terminating proxy separately for external clients.

### Local secure WebSockets

The WebSocket endpoints are transport-neutral and can run over either `ws`
or `wss`. The default `mvn jetty:run` setup remains HTTP-only for local
development. To enable Jetty's optional HTTPS connector as well as the normal
HTTP connector, provide a PKCS12 keystore and start with the `jetty.wss`
property:

```sh
mvn jetty:run \
  -Djetty.wss=true \
  -Djetty.ssl.keystore=/absolute/path/to/visitor.p12 \
  -Djetty.ssl.password='<keystore-password>'
```

This serves the same WebSocket endpoints over `ws://localhost:8080` and
`wss://localhost:8443`. Override the ports with `-Djetty.http.port=...` and
`-Djetty.ssl.port=...`. The keystore and password are supplied at runtime and
must not be committed. A browser also needs to trust the certificate and its
hostname must match the certificate.

In production, TLS is commonly terminated by the hosting platform or a reverse
proxy in front of the WAR. Configure that HTTPS listener to forward WebSocket
upgrade requests to the application. The `@ServerEndpoint` handlers need no
scheme-specific code; the TLS connector or proxy determines whether clients
connect using `wss://`.
