package com.visitor.server;

import com.google.protobuf.InvalidProtocolBufferException;
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

@ServerEndpoint(value = "/games/{playerId}/{gameID}")
public class GameEndpoint implements GameEndpointInterface {
    volatile Session session;
    UUID playerId;
    UUID gameID;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameID") String gameID, @PathParam("playerId") String playerId) throws IOException {
        this.session = session;
        this.playerId = UUID.fromString(playerId);
        this.gameID = UUID.fromString(gameID);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        session.setMaxIdleTimeout(0);
        GeneralEndpoint.getGameServer().addGameConnection(this.gameID, this.playerId, this);
    }

    @OnMessage(maxMessageSize = 5242880)
    public void onMessage(Session session, byte[] bytes) {
        try {
            gameServer.handleGameMessage(gameID, playerId, ClientGameMessage.parseFrom(bytes));
        } catch (InvalidProtocolBufferException | IllegalArgumentException e) {
            getLogger(GameEndpoint.class.getName()).log(SEVERE, "Invalid game message", e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (gameServer != null) gameServer.removeGameConnection(gameID, playerId, this);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        getLogger(GameEndpoint.class.getName()).log(SEVERE, "Game socket failed", error);
    }

    @Override
    public void send(ServerGameMessage.Builder builder, UUID targetId) throws IOException, EncodeException {
        if (!playerId.equals(targetId)) return;
        ServerGameMessage message = builder.build();
        gameServer.appendToHistory(gameID, message);
        Session current = session;
        if (current != null && current.isOpen()) {
            current.getBasicRemote().sendBinary(ByteBuffer.wrap(message.toByteArray()));
        }
    }

    @Override
    public void close() {
        Session current = session;
        session = null;
        try {
            if (current != null && current.isOpen()) current.close();
        } catch (IOException e) {
            getLogger(GameEndpoint.class.getName()).log(SEVERE, "Could not close game socket", e);
        }
    }

    @Override
    public void resendLastMessage(UUID targetId) throws IOException, EncodeException {
        ServerGameMessage message = gameServer.getLastMessage(gameID, targetId);
        if (message != null) send(message.toBuilder(), targetId);
    }
}
