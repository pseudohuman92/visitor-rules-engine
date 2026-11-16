package com.visitor.game.parts;

import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.ClientGameMessages.ClientGameMessage;
import com.visitor.server.GameEndpointInterface;

import java.util.UUID;

public class Connections extends HigherOrder {
    /**
     * Connection Methods
     * To deal with client connections
     */
    public void addConnection(UUID playerId, GameEndpointInterface connection) {
        connections.putIn(playerId, connection);
    }

    public void removeConnection(UUID playerId, GameEndpointInterface expected) {
        if (connections.get(playerId) == expected) {
            connections.removeFrom(playerId);
        }
    }

    public void setLastMessage(UUID playerId, ServerGameMessage lastMessage) {
        lastMessages.put(playerId, lastMessage);
    }

    public ServerGameMessage getLastMessage(UUID playerId) {
        return lastMessages.get(playerId);
    }

    public boolean acceptResponse(UUID senderId, ClientGameMessage message) {
        return !ended && pendingDecision != null && pendingDecision.accept(senderId, message);
    }


}
