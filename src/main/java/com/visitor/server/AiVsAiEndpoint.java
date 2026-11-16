package com.visitor.server;

import com.visitor.game.AiPlayer;
import com.visitor.game.parts.Game;
import com.visitor.protocol.ClientGameMessages.ClientGameMessage;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Read-only client stream for a match in which both seats are controlled by the server. */
@ServerEndpoint(value = "/ai-vs-ai/{playerId}/{gameID}/{aiId}")
public class AiVsAiEndpoint implements GameEndpointInterface {
    private static final Logger LOG = Logger.getLogger(AiVsAiEndpoint.class.getName());

    volatile Session session;
    UUID playerId;
    UUID gameID;
    UUID aiId;
    private byte[] lastSnapshot;
    private volatile boolean gameEndSent;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameID") String gameID,
                       @PathParam("playerId") String playerId, @PathParam("aiId") String aiId)
            throws IOException {
        this.session = session;
        this.playerId = UUID.fromString(playerId);
        this.gameID = UUID.fromString(gameID);
        this.aiId = UUID.fromString(aiId);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        session.setMaxIdleTimeout(0);
        GeneralEndpoint.getGameServer().addGameConnections(this.gameID, this, this.playerId, this.aiId);
    }

    /** Spectator actions are intentionally ignored; only the two server AIs may act. */
    @OnMessage(maxMessageSize = 5242880)
    public void onMessage(Session session, byte[] bytes) {
        try {
            ClientGameMessage.parseFrom(bytes);
            Session current = this.session;
            if (current != null && current.isOpen()) {
                current.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY,
                        "AI mirror matches are read-only"));
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Invalid spectator message", e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (GeneralEndpoint.gameServer != null) {
            GeneralEndpoint.gameServer.removeGameConnection(gameID, playerId, this);
            GeneralEndpoint.gameServer.removeGameConnection(gameID, aiId, this);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOG.log(Level.SEVERE, "AI mirror spectator connection failed", error);
    }

    @Override
    public void send(ServerGameMessage.Builder builder, UUID targetId) throws IOException, EncodeException {
        ServerGameMessage message = builder.build();
        GeneralEndpoint.getGameServer().appendToHistory(gameID, message);

        if (message.hasGameEnd()) {
            if (playerId.equals(targetId)) {
                sendToSpectator(message);
                gameEndSent = true;
            }
        } else if (message.hasUpdateGameStateP()) {
            sendSnapshot();
        }

        if (playerId.equals(targetId) || aiId.equals(targetId)) {
            ClientGameMessage response = AiPlayer.getResponse(message, targetId);
            if (response != null) {
                GeneralEndpoint.getGameServer().handleGameMessage(gameID, targetId, response);
            }
        }
    }

    private void sendSnapshot() throws IOException {
        Game game = GeneralEndpoint.getGameServer().games.get(gameID);
        if (game == null || game.isEnded()) return;
        ServerGameMessage snapshot = ServerGameMessage.newBuilder()
                .setUpdateGameStateP(com.visitor.protocol.ServerGameMessages.UpdateGameStateP.newBuilder()
                        .setGame(game.toGameState(playerId, false))).build();
        byte[] bytes = snapshot.toByteArray();
        if (!Arrays.equals(lastSnapshot, bytes)) {
            lastSnapshot = bytes;
            sendToSpectator(snapshot);
        }
    }

    private void sendToSpectator(ServerGameMessage message) throws IOException {
        Session current = session;
        if (current != null && current.isOpen()) {
            current.getBasicRemote().sendBinary(ByteBuffer.wrap(message.toByteArray()));
        }
    }

    @Override
    public void close() {
        // Keep the read-only socket open after the final result so the client
        // can consume GameEnd and display its winner screen. The client closes
        // the connection when it leaves that screen.
        if (gameEndSent) return;
        Session current = session;
        session = null;
        try {
            if (current != null && current.isOpen()) current.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not close AI mirror spectator socket", e);
        }
    }

    @Override
    public void resendLastMessage(UUID targetId) throws IOException, EncodeException {
        ServerGameMessage message = GeneralEndpoint.getGameServer().getLastMessage(gameID, targetId);
        if (message != null) send(message.toBuilder(), targetId);
    }
}
