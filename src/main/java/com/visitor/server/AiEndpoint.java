package com.visitor.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.visitor.game.AiPlayer;
import com.visitor.protocol.ClientGameMessages.ClientGameMessage;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

import static com.visitor.server.GeneralEndpoint.gameServer;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

@ServerEndpoint(value = "/ai/{playerId}/{gameID}/{aiId}")
public class AiEndpoint implements GameEndpointInterface {
    volatile Session session;
    UUID playerId;
    UUID gameID;
    UUID aiId;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameID") String gameID, @PathParam("playerId") String playerId, @PathParam("aiId") String aiId) throws IOException {
        this.session = session;
        this.playerId = UUID.fromString(playerId);
        this.gameID = UUID.fromString(gameID);
        this.aiId = UUID.fromString(aiId);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        session.setMaxIdleTimeout(0);
        GeneralEndpoint.getGameServer().addGameConnections(this.gameID, this, this.playerId, this.aiId);
    }

    @OnMessage(maxMessageSize = 5242880)
    public void onMessage(Session session, byte[] bytes) {
        try {
            gameServer.handleGameMessage(gameID, playerId, ClientGameMessage.parseFrom(bytes));
        } catch (InvalidProtocolBufferException | IllegalArgumentException e) {
            getLogger(AiEndpoint.class.getName()).log(SEVERE, "Invalid game message", e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (gameServer != null) {
            gameServer.removeGameConnection(gameID, playerId, this);
            gameServer.removeGameConnection(gameID, aiId, this);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        getLogger(AiEndpoint.class.getName()).log(SEVERE, "AI game socket failed", error);
    }

    @Override
    public void send(ServerGameMessage.Builder builder, UUID targetId) throws IOException, EncodeException {
        ServerGameMessage message = builder.build();
        gameServer.appendToHistory(gameID, message);
        if (playerId.equals(targetId)) {
            Session current = session;
            if (current != null && current.isOpen()) {
                current.getBasicRemote().sendBinary(ByteBuffer.wrap(message.toByteArray()));
            }
        } else if (aiId.equals(targetId)) {
            ClientGameMessage response = AiPlayer.getResponse(message, aiId);
            if (response != null) gameServer.handleGameMessage(gameID, aiId, response);
        }
    }

    @Override
    public void close() {
        Session current = session;
        session = null;
        try {
            if (current != null && current.isOpen()) current.close();
        } catch (IOException e) {
            getLogger(AiEndpoint.class.getName()).log(SEVERE, "Could not close AI socket", e);
        }
    }

    @Override
    public void resendLastMessage(UUID targetId) throws IOException, EncodeException {
        ServerGameMessage message = gameServer.getLastMessage(gameID, targetId);
        if (message != null) send(message.toBuilder(), targetId);
    }
}
