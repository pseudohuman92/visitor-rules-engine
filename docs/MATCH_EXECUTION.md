# Match execution and effect ordering

## Match ownership

Each live `Game` owns a lazy, single-thread `MatchExecutor`. Once a match is
published in `GameServer.games`, game commands, clock ticks, prompt responses,
connection changes, history writes, and termination run on that worker.
Construction and deck/player initialization happen before publication. Direct
model methods remain imperative APIs: callers changing a published match must
submit through `executeAction` or `executeControl`.

Ordinary actions run in submission order and never run recursively inside
another action. Separate matches have separate workers. WebSocket callbacks
decode immutable protobuf messages and enqueue work; they do not create a
thread for each game message. Normal AI commands use the same queue, including
commands generated while sending a snapshot.

Server action methods return `CompletableFuture<Void>`. Callers that depend on
completion must await the future outside the match worker. Do not join a queued
ordinary action from its own worker.

## Decisions and control work

A resolving effect can wait for a player decision. During that wait, the same
worker pumps a control queue for responses, reconnect/disconnect, clock ticks,
history, and termination. Ordinary actions stay queued until the effect returns.
Outside a decision, submitted controls and ordinary actions retain queue order;
controls called by the worker itself execute inline.

The match registers one `PendingDecision` before sending its prompt, allowing an
immediate AI reply. Responses must match the expected player and payload type
(and selection subtype where applicable). X values and knowledge choices are
checked against the prompt. Unsolicited responses are discarded rather than
buffered for a later prompt. This does not add wire-level decision IDs or fully
validate every selection/damage assignment; a delayed response of the same type
cannot be distinguished from a reply to a later identical prompt.

Reconnect replays the player's cached message, including a pending prompt, on
the worker. Disconnect removes a connection only if it is still the expected
endpoint, so a stale socket cannot remove its replacement. Cached protobuf
messages are immutable and stored in a concurrent map; external readers see
completed snapshots instead of serializing partially changed state.

Concession and timeout can end a match during a decision. Termination sends the
terminal messages once, cancels the pending decision, closes connections,
interrupts player clocks, and shuts down the worker. Queued ordinary actions
check the ended flag and become no-ops. Clock threads only schedule ticks; time
updates and timeout callbacks belong to the match worker.

Socket sends still occur on the owner worker, so a slow send can delay that
match. Drafts use their existing separate runtime; this ownership contract
describes live `Game` matches.

## Effect phases

Effect UUIDs identify effects. They do not determine execution order. Effect
maps use `LinkedHashMap`, retaining the order in which card definitions register
their effects. Targeting descriptions follow the same registration order within
each phase, regardless of the order of target selections in a client message.

| Operation | Execution order |
| --- | --- |
| Default card play | Printed energy payment when requested, additional cost effects in registration order, stack insertion, additional play hooks in registration order |
| Activated ability | Additional cost effects in registration order, printed energy payment, depletion if configured, sacrifice if configured, ability stack insertion |
| Card resolution | Resolution effects in registration order, placement into the resolve zone, after-resolution effects in registration order |
| Ability resolution | Ability effects in registration order |
| Ability presentation | Permanent abilities in registration order, then temporary abilities in registration order |

The existing difference between card and ability payment phases is preserved.
For example, Shield Barrage registers shield removal before damage; resolution
now consistently removes the shield before dealing damage based on that amount.

## Events and the stack

Events in the current processing batch are visited FIFO. For each event, the
turn player's registered triggering cards are checked before the opponent's;
cards and each card's checkers run in registration order. A checker normally
executes its callback immediately. Explicit nested calls to `processEvents`
execute inline; newly queued events are handled by a later processing call
unless code explicitly processes them immediately.

A trigger that explicitly creates an ability places it on the stack. The stack
resolves LIFO: abilities inserted A then B resolve B then A. Registration order
therefore determines insertion order, while the stack determines resolution
order. This change does not introduce a new simultaneous-trigger choice system.

## Verification

`mvn verify` runs 65 tests. Regression coverage includes queued actions during
decisions, response ownership, reconnect replay, stale disconnects, concession,
clock timeout, independent matches, AI callbacks, cached snapshots, worker
shutdown, and queue order outside decisions. Effect tests exercise ordered
costs, resolution and placement, activated abilities, real Shield Barrage
behavior, immediate triggers, and explicit LIFO triggered abilities.
