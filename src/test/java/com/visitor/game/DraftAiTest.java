package com.visitor.game;

import com.visitor.card.Card;
import com.visitor.game.parts.Game;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.Types;
import com.visitor.server.DraftEndpoint;
import com.visitor.sets.base2.HitTeam;
import com.visitor.sets.base2.NeuralAnalyst;
import com.visitor.sets.base2.SkyMarcher;
import com.visitor.sets.base2.Brute;
import com.visitor.sets.base2.ArtofWar;
import com.visitor.sets.base2.Naturality;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class DraftAiTest {
    @Test public void observesFivePacksBeforeChoosingTwoOrThreeColors() {
        DraftAi ai = new DraftAi();
        Game game = new Game();
        Player player = new Player(game, "drafter", new String[0]);
        Player opponent = new Player(game, "other", new String[0]);
        game.addPlayers(player, opponent);
        game.stopActiveClock();
        player.clock.interrupt();
        opponent.clock.interrupt();
        UUID owner = player.getId();
        try {
            List<Card> drafted = new ArrayList<>();
            for (int round = 0; round < 5; round++) {
                List<Card> pack = Arrays.asList(new HitTeam(game, owner),
                        new NeuralAnalyst(game, owner), new SkyMarcher(game, owner));
                drafted.add(ai.choose(pack, drafted, round));
                drafted.add(ai.choose(Arrays.asList(new HitTeam(game, owner),
                        new NeuralAnalyst(game, owner)), drafted, round));
                assertEquals(round + 1, ai.getPacksSeen());
                assertTrue(ai.getCommittedColors().isEmpty());
            }
            Card inColor = new HitTeam(game, owner);
            Card outOfColor = new Brute(game, owner);
            Card choice = ai.choose(Arrays.asList(inColor, outOfColor), drafted, 5);
            Set<Types.Knowledge> palette = ai.getCommittedColors();
            assertTrue(palette.size() == 2 || palette.size() == 3);
            assertTrue(palette.contains(Types.Knowledge.RED));
            assertTrue(palette.contains(Types.Knowledge.BLUE));
            assertSame(inColor, choice);
            Card matchingTome = new ArtofWar(game, owner);
            Card otherTome = new Naturality(game, owner);
            assertSame(matchingTome, ai.choose(Arrays.asList(matchingTome, otherTome), drafted, 5));
            assertEquals(6, ai.getPacksSeen());
        } finally {
            player.clock.interrupt();
            opponent.clock.interrupt();
        }
    }

    private static class CaptureEndpoint extends DraftEndpoint {
        ServerGameMessage latest;
        @Override public void send(ServerGameMessage.Builder builder) { latest = builder.build(); }
    }

    @Test(timeout=30000) public void aiCompletesFortyCardDraftWithOneHumanConnection() throws Exception {
        Draft draft = new Draft("human", "Draft AI", true);
        try {
            UUID human = draft.players.values().stream()
                    .filter(p -> p.username.equals("human")).findFirst().get().getId();
            CaptureEndpoint endpoint = new CaptureEndpoint();
            draft.addConnection(human, endpoint);
            for (int pick = 0; pick < Draft.TOTAL_ROUNDS * 2; pick++) {
                assertFalse(endpoint.latest.getPickCard().getDraft().getCompleted());
                assertEquals(pick % 2 == 0 ? 3 : 2,
                        endpoint.latest.getPickCard().getCandidatesCount());
                UUID selected = UUID.fromString(endpoint.latest.getPickCard()
                        .getCandidates(0).getId());
                draft.draftPick(human, selected);
            }
            assertTrue(endpoint.latest.getPickCard().getDraft().getCompleted());
            assertEquals(40, draft.players.get(human).deck.size());
            assertTrue(draft.getDecklist("human").length > 0);
            assertEquals(40, draft.players.get(draft.getOpponentId(human)).deck.size());
            assertTrue(draft.isCompleted());
            assertEquals(20, draft.getDraftAi().getPacksSeen());
            assertTrue(draft.getDraftAi().getCommittedColors().size() >= 2);
            assertTrue(draft.getDraftAi().getCommittedColors().size() <= 3);
        } finally {
            for (Player player : draft.players.values()) player.clock.interrupt();
            for (Player player : draft.players.values()) player.clock.join(1000);
        }
    }
}
