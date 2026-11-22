package com.visitor.sets.base2;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class ExperimentalDrone extends Unit {
    public ExperimentalDrone(Game game, UUID owner) {
        super(game, "Experimental Drone", 2, new CounterMap<>(BLUE, 1),
                "Research 1. When Experimental Drone enters play, draw a card.", 2, 2, owner);
        setResearchRequirement(1);
        playable.addAfterResolveEffect(() -> game.draw(controller, 1));
    }
}
