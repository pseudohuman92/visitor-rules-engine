package com.visitor.game;

import com.visitor.card.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.HelperFunctions;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.server.DraftEndpoint;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class CardCatalogTest {
    @Test public void aiDeckContainsAllFortyEightConfiguredCurrentCards() throws Exception {
        AiPlayer ai=new AiPlayer(new Game());
        try {
            assertEquals(48,ai.deck.size());
            for (Card card : ai.deck) {
                assertEquals("com.visitor.sets."+HelperFunctions.COLLECTION_SET,card.getClass().getPackageName());
                assertEquals(ai.getId(),card.controller);
            }
        } finally {
            ai.clock.interrupt(); ai.clock.join(1000);
        }
    }

    @Test public void allDraftCatalogEntriesCanBeConstructed() {
        assertTrue(Draft.cardClasses.size()>=3);
        Game game=new Game();
        UUID owner=UUID.randomUUID();
        for (String name : Draft.cardClasses) {
            assertNotNull(name,HelperFunctions.createCard(game,owner,HelperFunctions.COLLECTION_SET+"."+name));
        }
    }

    private static class CaptureEndpoint extends DraftEndpoint {
        ServerGameMessage latest;
        @Override public void send(ServerGameMessage.Builder builder) { latest=builder.build(); }
    }

    @Test public void staleDraftSocketCannotRemoveItsReplacement() {
        Draft draft = new Draft("first", "second");
        UUID first = draft.players.values().stream().filter(p -> p.username.equals("first")).findFirst().get().getId();
        CaptureEndpoint old = new CaptureEndpoint(), replacement = new CaptureEndpoint();
        draft.addConnection(first, old);
        draft.addConnection(first, replacement);
        draft.removeConnection(first, old);
        assertSame(replacement, draft.connections.get(first));
        draft.removeConnection(first, replacement);
        assertFalse(draft.connections.containsKey(first));
    }

    @Test(timeout=10000) public void threeRoundDraftCompletesAndExportsLoadableDecks() throws Exception {
        Draft draft=new Draft("first","second");
        try {
            UUID first=draft.players.values().stream().filter(p -> p.username.equals("first")).findFirst().get().getId();
            UUID second=draft.getOpponentId(first);
            CaptureEndpoint a=new CaptureEndpoint(), b=new CaptureEndpoint();
            draft.addConnection(first,a); draft.addConnection(second,b);
            for (int pick=0; pick<6; pick++) {
                assertFalse(a.latest.getPickCard().getDraft().getCompleted());
                assertEquals(pick%2==0 ? 3 : 2,a.latest.getPickCard().getCandidatesCount());
                UUID aCard=UUID.fromString(a.latest.getPickCard().getCandidates(0).getId());
                UUID bCard=UUID.fromString(b.latest.getPickCard().getCandidates(0).getId());
                draft.draftPick(first,UUID.randomUUID());
                draft.draftPick(first,aCard);
                draft.draftPick(first,aCard);
                draft.draftPick(second,bCard);
            }
            assertTrue(a.latest.getPickCard().getDraft().getCompleted());
            assertTrue(b.latest.getPickCard().getDraft().getCompleted());
            for (Player player : draft.players.values()) {
                assertEquals(6,player.deck.size());
                String[] exported=player.deck.toDeckList().toArray(new String[0]);
                for (String line : exported) assertTrue(line.contains(";base2."));
                Deck restored=new Deck(new Game(),player.getId(),exported);
                assertEquals(6,restored.size());
                assertEquals(player.deck.toDeckList(),restored.toDeckList());
            }
        } finally {
            for (Player player : draft.players.values()) player.clock.interrupt();
            for (Player player : draft.players.values()) player.clock.join(1000);
        }
    }
}
