package com.visitor.sets.base2;

import com.visitor.card.types.Ritual;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class ExperimentalBreakthrough extends Ritual {
    public ExperimentalBreakthrough(Game game, UUID owner) {
        super(game, "Experimental Breakthrough", 3, new CounterMap<>(BLUE, 2),
                "Research 2. Draw 3 cards. Gain 2 energy this turn.", owner);
        setResearchRequirement(2);
        playable.addResolveEffect(() -> {
            game.draw(controller, 3);
            game.addEnergy(controller, 2, true);
        });
    }
}
