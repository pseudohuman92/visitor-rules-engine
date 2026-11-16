package com.visitor.server;

import com.visitor.game.AiPlayer;
import com.visitor.game.Draft;
import com.visitor.game.Player;
import com.visitor.game.IllegalGameActionException;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.ServerMessages;
import com.visitor.protocol.ServerMessages.LoginResponse;
import com.visitor.protocol.ServerMessages.NewGame;
import com.visitor.protocol.ServerMessages.ServerMessage;
import com.visitor.protocol.Types;
import com.visitor.protocol.ClientGameMessages.ClientGameMessage;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;

import static com.visitor.game.IllegalGameActionException.parseId;

import static com.visitor.protocol.Types.GameType.AI_BO1_CONSTRUCTED;
import static com.visitor.protocol.Types.GameType.AI_VS_AI;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 * @author pseudo
 */
public class GameServer {

    //static MetricsDb metricsDb = new MetricsDb();
    public ConcurrentMap<String, GeneralEndpoint> playerConnections;
    public ConcurrentMap<UUID, Game> games;
    public ConcurrentMap<UUID, Draft> drafts;
    public Arraylist<String> chatLog;
    public Arraylist<QueuePlayer> Bo1ConstructedQueue;
    public Arraylist<QueuePlayer> P2DraftQueue;
    public Hashmap<UUID, QueuePlayer> P2DraftGameQueue;

    public GameServer() {
        playerConnections = new ConcurrentHashMap<>();
        chatLog = new Arraylist<>();
        games = new ConcurrentHashMap<>();
        drafts = new ConcurrentHashMap<>();
        Bo1ConstructedQueue = new Arraylist<>();
        P2DraftQueue = new Arraylist<>();
        P2DraftGameQueue = new Hashmap<>();
    }

    private CompletableFuture<Void> observe(CompletableFuture<Void> future) {
        return future.whenComplete((ignored, failure) -> {
            if (failure != null && !(failure instanceof CancellationException)
                    && !(failure instanceof RejectedExecutionException)) {
                getLogger(GameServer.class.getName()).log(SEVERE, "Match task failed", failure);
            }
        });
    }

    private CompletableFuture<Void> takeGameAction(UUID gameID, UUID playerId, Consumer<Game> action) {
        Game game = games.get(gameID);
        if (game == null) return CompletableFuture.completedFuture(null);
        return observe(game.executeAction(() -> {
            if (games.get(gameID) != game) return;
            game.stopActiveClock();
            try {
                action.accept(game);
                if (!game.isEnded()) game.updatePlayers();
            } catch (IllegalGameActionException e) {
                getLogger(GameServer.class.getName()).warning("Rejected game action: " + e.getMessage());
                game.updatePlayer(playerId);
            } catch (CancellationException e) {
                if (!game.isEnded()) throw e;
            } finally {
                if (!game.isEnded() && games.get(gameID) == game) game.startActiveClock();
            }
        }));
    }

    /** Socket callbacks only decode messages; the match worker owns dispatch and state. */
    CompletableFuture<Void> handleGameMessage(UUID gameID, UUID sender, ClientGameMessage message) {
        Game game = games.get(gameID);
        if (game == null) return CompletableFuture.completedFuture(null);
        switch (message.getPayloadCase()) {
            case CONCEDE:
            case ORDERCARDSRESPONSE:
            case SELECTFROMRESPONSE:
            case SELECTXVALUERESPONSE:
            case ASSIGNDAMAGERESPONSE:
            case SELECTKNOWLEDGERESPONSE:
                return observe(game.executeControl(() -> {
                    if (game.isEnded()) return;
                    game.addToHistory(message);
                    if (message.hasConcede()) {
                        if (game.isPlayer(sender)) game.gameEnd(sender, false);
                    } else {
                        game.acceptResponse(sender, message);
                    }
                }));
            default:
                return takeGameAction(gameID, sender, current -> {
                    current.addToHistory(message);
                    switch (message.getPayloadCase()) {
                        case PLAYCARD:
                            current.playCard(sender, parseId(message.getPlayCard().getCardId()),
                                    new Arraylist<>(message.getPlayCard().getTargetsList()));
                            break;
                        case ACTIVATECARD:
                            current.activateCard(sender, parseId(message.getActivateCard().getCardId()),
                                    parseId(message.getActivateCard().getAbilityId()),
                                    new Arraylist<>(message.getActivateCard().getTargetsList()));
                            break;
                        case STUDYCARD:
                            Types.Knowledge knowledge = message.getStudyCard().getSelectedKnowledge();
                            CounterMap<Types.Knowledge> gain = new CounterMap<>();
                            if (knowledge != Types.Knowledge.NONE) gain.add(knowledge, 1);
                            current.studyCard(sender, parseId(message.getStudyCard().getCardId()), true, gain);
                            break;
                        case PASS: current.pass(sender); break;
                        case KEEP: current.keep(sender); break;
                        case REDRAW: current.redraw(sender); break;
                        case SELECTATTACKERS:
                            current.selectAttackers(sender, message.getSelectAttackers().getAttackersList().toArray(new Types.AttackerAssignment[0]));
                            break;
                        case SELECTBLOCKERS:
                            current.selectBlockers(sender, message.getSelectBlockers().getBlockersList().toArray(new Types.BlockerAssignment[0]));
                            break;
                        default: throw new IllegalGameActionException("Unexpected game command");
                    }
                });
        }
    }

    CompletableFuture<Void> activateCard(UUID gameID, UUID playerId, UUID cardID, UUID abilityId, Arraylist<Types.TargetSelection> targets) {
        return takeGameAction(gameID, playerId, game -> game.activateCard(playerId, cardID, abilityId, targets));
    }

    CompletableFuture<Void> concede(UUID gameID, UUID playerId) {
        return handleGameMessage(gameID, playerId, ClientGameMessage.newBuilder()
                .setConcede(com.visitor.protocol.ClientGameMessages.Concede.newBuilder()).build());
    }

    CompletableFuture<Void> redraw(UUID gameID, UUID playerId) {
        return takeGameAction(gameID, playerId, game -> game.redraw(playerId));
    }

    CompletableFuture<Void> keep(UUID gameID, UUID playerId) {
        return takeGameAction(gameID, playerId, game -> game.keep(playerId));
    }

    CompletableFuture<Void> pass(UUID gameID, UUID playerId) {
        return takeGameAction(gameID, playerId, game -> game.pass(playerId));
    }

    CompletableFuture<Void> studyCard(UUID gameID, UUID playerId, UUID cardID, CounterMap<Types.Knowledge> knowledge) {
        return takeGameAction(gameID, playerId, game -> game.studyCard(playerId, cardID, true, knowledge));
    }

    CompletableFuture<Void> playCard(UUID gameID, UUID playerId, UUID cardID, Arraylist<Types.TargetSelection> targets) {
        return takeGameAction(gameID, playerId, game -> game.playCard(playerId, cardID, targets));
    }

    CompletableFuture<Void> selectAttackers(UUID gameID, UUID playerId, Types.AttackerAssignment[] attackers) {
        return takeGameAction(gameID, playerId, game -> game.selectAttackers(playerId, attackers));
    }

    CompletableFuture<Void> selectBlockers(UUID gameID, UUID playerId, Types.BlockerAssignment[] blockers) {
        return takeGameAction(gameID, playerId, game -> game.selectBlockers(playerId, blockers));
    }

    ServerGameMessage getLastMessage(UUID gameID, UUID playerId) {
        Game game = games.get(gameID);
        return game == null ? null : game.getLastMessage(playerId);
    }

    synchronized void addConnection(String username, GeneralEndpoint connection) {
        try {
            playerConnections.put(username, connection);
            final UUID[] playerGame = {null};
            final UUID[] playerId = {null};
            games.forEach((id, game) -> {
                if (game.isPlayerInGame(username)) {
                    playerGame[0] = id;
                    playerId[0] = game.getPlayerId(username);
                }
            });
            connection.send(ServerMessage.newBuilder().setLoginResponse(LoginResponse.newBuilder()
                    .setGameId(playerGame[0] != null ? playerGame[0].toString() : "")
                    .setPlayerId(playerId[0] != null ? playerId[0].toString() : "")));
        } catch (IOException | EncodeException ex) {
            getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
        }
    }

    synchronized void removeConnection(String username) {
        playerConnections.remove(username);
        for (int i = 0; i < Bo1ConstructedQueue.size(); i++) {
            QueuePlayer p = Bo1ConstructedQueue.get(i);
            if (p.username.equals(username)) {
                Bo1ConstructedQueue.remove(p);
                i--;
            }
        }
        for (int i = 0; i < P2DraftQueue.size(); i++) {
            QueuePlayer p = P2DraftQueue.get(i);
            if (p.username.equals(username)) {
                P2DraftQueue.remove(p);
                i--;
            }
        }
    }

    CompletableFuture<Void> addGameConnection(UUID gameID, UUID playerId, GameEndpointInterface connection) {
        return addGameConnections(gameID, connection, playerId);
    }

    CompletableFuture<Void> addGameConnections(UUID gameID, GameEndpointInterface connection, UUID... players) {
        Game game = games.get(gameID);
        if (game == null) {
            connection.close();
            return CompletableFuture.completedFuture(null);
        }
        return observe(game.executeControl(() -> {
            if (game.isEnded()) { connection.close(); return; }
            for (UUID player : players) {
                if (!game.isPlayer(player)) { connection.close(); return; }
            }
            for (UUID player : players) game.addConnection(player, connection);
            for (UUID player : players) {
                try {
                    connection.resendLastMessage(player);
                } catch (IOException | EncodeException e) {
                    getLogger(GameServer.class.getName()).log(SEVERE, "Could not resume connection", e);
                }
            }
        }));
    }

    CompletableFuture<Void> removeGameConnection(UUID gameID, UUID playerId, GameEndpointInterface expected) {
        Game game = games.get(gameID);
        if (game == null) return CompletableFuture.completedFuture(null);
        return observe(game.executeControl(() -> game.removeConnection(playerId, expected)));
    }

    private void registerGame(Game game, Runnable notifyPlayers) {
        games.put(game.getId(), game);
        observe(game.executeControl(() -> { if (!game.isEnded()) notifyPlayers.run(); }));
    }

    synchronized void joinQueue(String username, Types.GameType gameType, String[] decklist, UUID draftId) {
        switch (gameType) {
            case AI_BO1_CONSTRUCTED:
                startAiGame(username, decklist);
                break;
            case AI_VS_AI:
                startAiVsAiGame(username, decklist);
                break;
            case BO1_CONSTRUCTED:
                join1Bo1ConstructedQueue(username, decklist);
                break;
            case P2_DRAFT:
                joinP2DraftQueue(username);
                break;
            case P2_DRAFT_GAME:
                joinP2DraftGameQueue(draftId, username, decklist);
                break;
        }

    }

    private void joinP2DraftGameQueue(UUID draftId, String username, String[] decklist) {
        if (!P2DraftGameQueue.containsKey(draftId)) {
            System.out.println("Adding " + username + " to draft game queue!");
            P2DraftGameQueue.put(draftId, new QueuePlayer(username, decklist));
        } else {
            if (P2DraftGameQueue.get(draftId).username.equals(username)) {
                return;
            }
            QueuePlayer waitingPlayer = P2DraftGameQueue.remove(draftId);
            System.out.println("Starting a new draft game with " + username + " and " + waitingPlayer.username);
            Game game = new Game();
            game.addToHistory("Game Type: Draft");
            game.addToHistory(waitingPlayer.username + ":\n" + Arrays.toString(waitingPlayer.decklist));
            game.addToHistory(username + ":\n" + Arrays.toString(decklist));
            game.addPlayers(new Player(game, waitingPlayer.username, waitingPlayer.decklist), new Player(game, username, decklist));
            registerGame(game, () -> {
                try {
                    playerConnections.get(waitingPlayer.username).send(ServerMessage.newBuilder()
                            .setNewGame(NewGame.newBuilder()
                                    .setGame(game.toGameState(waitingPlayer.username))));
                    playerConnections.get(username).send(ServerMessage.newBuilder()
                            .setNewGame(NewGame.newBuilder()
                                    .setGame(game.toGameState(username))));
                } catch (IOException | EncodeException ex) {
                    getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
                }
            });
        }
    }

    private void joinP2DraftQueue(String username) {
        if (P2DraftQueue.isEmpty()) {
            System.out.println("Adding " + username + " to draft queue!");
            P2DraftQueue.add(new QueuePlayer(username, null));
        } else {
            if (P2DraftQueue.get(0).username.equals(username)) {
                return;
            }
            QueuePlayer waitingPlayer = P2DraftQueue.remove(0);
            System.out.println("Starting a new draft with " + username + " and " + waitingPlayer.username);
            Draft draft = new Draft(waitingPlayer.username, username);
            drafts.put(draft.getId(), draft);
            try {
                playerConnections.get(waitingPlayer.username).send(ServerMessage.newBuilder()
                        .setNewDraft(ServerMessages.NewDraft.newBuilder()
                                .setDraft(draft.toDraftState(waitingPlayer.username, false))));
                playerConnections.get(username).send(ServerMessage.newBuilder()
                        .setNewDraft(ServerMessages.NewDraft.newBuilder()
                                .setDraft(draft.toDraftState(username, false))));
            } catch (IOException | EncodeException ex) {
                getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
            }
        }
    }

    private void join1Bo1ConstructedQueue(String username, String[] decklist) {
        if (Bo1ConstructedQueue.isEmpty()) {
            System.out.println("Adding " + username + " to constructed game queue!");
            Bo1ConstructedQueue.add(new QueuePlayer(username, decklist));
        } else {
            if (Bo1ConstructedQueue.get(0).username.equals(username)) {
                return;
            }
            QueuePlayer waitingPlayer = Bo1ConstructedQueue.remove(0);
            System.out.println("Starting a new constructed game with " + username + " and " + waitingPlayer.username);
            Game game = new Game();
            game.addToHistory("Game Type: Best-of-1");
            game.addToHistory(waitingPlayer.username + ":\n" + Arrays.toString(waitingPlayer.decklist));
            game.addToHistory(username + ":\n" + Arrays.toString(decklist));
            game.addPlayers(new Player(game, waitingPlayer.username, waitingPlayer.decklist), new Player(game, username, decklist));
            registerGame(game, () -> {
                try {
                    playerConnections.get(waitingPlayer.username).send(ServerMessage.newBuilder()
                            .setNewGame(NewGame.newBuilder()
                                    .setGame(game.toGameState(waitingPlayer.username))));
                    playerConnections.get(username).send(ServerMessage.newBuilder()
                            .setNewGame(NewGame.newBuilder()
                                    .setGame(game.toGameState(username))));
                } catch (IOException | EncodeException ex) {
                    getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
                }
            });
        }
    }

    private void startAiGame(String username, String[] decklist) {
        System.out.println("Starting a new AI constructed game with " + username);
        Game game = new Game();
        game.addToHistory("Game Type: AI Best-of-1");
        game.addToHistory(username + ":\n" + Arrays.toString(decklist));
        AiPlayer aiPlayer = new AiPlayer(game);
        game.addPlayers(aiPlayer, new Player(game, username, decklist));
        registerGame(game, () -> {
            try {
                playerConnections.get(username).send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGameType(AI_BO1_CONSTRUCTED)
                                .setAiId(aiPlayer.getId().toString())
                                .setGame(game.toGameState(username))));
            } catch (IOException | EncodeException ex) {
                getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
            }
        });
    }

    private void startAiVsAiGame(String username, String[] decklist) {
        String[] mirrorDeck = decklist == null || decklist.length == 0
                ? AiPlayer.defaultDecklist() : Arrays.copyOf(decklist, decklist.length);
        Game game = new Game();
        AiPlayer first = new AiPlayer(game, "Mirror AI Alpha", mirrorDeck);
        AiPlayer second = new AiPlayer(game, "Mirror AI Omega", mirrorDeck);
        game.addToHistory("Game Type: AI vs AI mirror match");
        game.addToHistory(Arrays.toString(mirrorDeck));
        game.addPlayers(first, second);
        registerGame(game, () -> {
            try {
                GeneralEndpoint connection = playerConnections.get(username);
                if (connection == null) {
                    games.remove(game.getId(), game);
                    game.close();
                    return;
                }
                connection.send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGameType(AI_VS_AI)
                                .setAiId(second.getId().toString())
                                .setGame(game.toGameState(first.getId(), false))));
            } catch (IOException | EncodeException ex) {
                getLogger(GameServer.class.getName()).log(SEVERE, "Could not start AI mirror match", ex);
                games.remove(game.getId(), game);
                game.close();
            }
        });
    }

    public void removeGame(UUID gameID, Arraylist<String> history) {
        Game game = games.get(gameID);
        if (game != null) {
            observe(game.executeControl(() -> {
                games.remove(gameID, game);
                game.close();
            }));
        }
    }

    synchronized public void addDraftConnection(UUID draftID, UUID playerId, DraftEndpoint connection) {
        drafts.get(draftID).addConnection(playerId, connection);
    }

    synchronized public void removeDraftConnection(UUID draftID, UUID playerId) {
        drafts.get(draftID).removeConnection(playerId);
    }

    public void draftPick(UUID draftID, UUID playerId, UUID cardId) {
        System.out.println("Start Draft Pick");
        drafts.get(draftID).draftPick(playerId, cardId);
        System.out.println("End Draft Pick");
    }

    public ServerGameMessage getDraftLastMessage(UUID draftID, UUID playerId) {
        return drafts.get(draftID).getLastMessage(playerId);
    }

    public CompletableFuture<Void> appendToHistory(UUID gameID, Object message) {
        Game game = games.get(gameID);
        if (game == null) return CompletableFuture.completedFuture(null);
        return observe(game.executeControl(() -> game.addToHistory(message)));
    }
}
