package com.visitor.server;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameServerDraftQueueTest {
    @Test public void disconnectRemovesPlayerFromDraftMatchQueue() {
        GameServer server = new GameServer();
        UUID abandoned = UUID.randomUUID();
        UUID waiting = UUID.randomUUID();
        server.P2DraftGameQueue.put(abandoned, new QueuePlayer("left", new String[] {"1;base2.Bloodrunner"}));
        server.P2DraftGameQueue.put(waiting, new QueuePlayer("still-here", new String[] {"1;base2.Bloodrunner"}));

        server.removeConnection("left");

        assertFalse(server.P2DraftGameQueue.containsKey(abandoned));
        assertTrue(server.P2DraftGameQueue.containsKey(waiting));
    }

    @Test public void staleProfileSocketCannotRemoveDraftMatchQueueEntry() {
        GameServer server = new GameServer();
        UUID draft = UUID.randomUUID();
        GeneralEndpoint old = new GeneralEndpoint(), replacement = new GeneralEndpoint();
        server.playerConnections.put("player", replacement);
        server.P2DraftGameQueue.put(draft, new QueuePlayer("player", new String[] {"1;base2.Bloodrunner"}));

        server.removeConnection("player", old);
        assertTrue(server.P2DraftGameQueue.containsKey(draft));
        assertTrue(server.playerConnections.get("player") == replacement);

        server.removeConnection("player", replacement);
        assertFalse(server.P2DraftGameQueue.containsKey(draft));
    }
}
