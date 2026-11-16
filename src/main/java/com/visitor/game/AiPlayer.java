package com.visitor.game;

import com.visitor.game.parts.Game;
import com.visitor.protocol.ClientGameMessages.ClientGameMessage;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;

import java.util.Arrays;
import java.util.UUID;

/** Server-controlled player whose decisions are made by {@link AiStrategy}. */
public class AiPlayer extends Player {
    private static final String[] DEFAULT_DECKLIST = {
            "3;base2.TacticalBriefing",
            "3;base2.BlitzRobbers",
            "3;base2.ExplosiveShot",
            "3;base2.PuncturingRounds",
            "3;base2.Stormtrooper",
            "3;base2.FireAtWill",
            "3;base2.FlameStrike",
            "3;base2.HitTeam",
            "3;base2.Prefire",
            "3;base2.TacticalGenius",
            "3;base2.Barracks",
            "3;base2.DeploymentTruck",
            "3;base2.FlameSweep",
            "3;base2.SkyMarcher",
            "3;base2.AmmoFactory",
            "3;base2.NuclearBlast"};

    public AiPlayer(Game game) {
        this(game, "AI Player", DEFAULT_DECKLIST);
    }

    public AiPlayer(Game game, String username, String[] decklist) {
        super(game, username, decklist == null || decklist.length == 0
                ? Arrays.copyOf(DEFAULT_DECKLIST, DEFAULT_DECKLIST.length)
                : Arrays.copyOf(decklist, decklist.length));
    }

    public static String[] defaultDecklist() {
        return Arrays.copyOf(DEFAULT_DECKLIST, DEFAULT_DECKLIST.length);
    }

    public static ClientGameMessage getResponse(ServerGameMessage message, UUID aiId) {
        return AiStrategy.respond(message, aiId);
    }
}
