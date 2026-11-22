package com.visitor.sets.base2;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class RealityArchitect extends Unit {
    public RealityArchitect(Game game, UUID owner) {
        super(game, "Reality Architect", 4, new CounterMap<>(BLUE, 2),
                "Research 2. When Reality Architect enters play, gain energy this turn equal to the number of times it was studied.",
                3, 4, owner);
        setResearchRequirement(2);
        playable.addAfterResolveEffect(() -> game.addEnergy(controller, getActualResearchStudies(), true));
    }
}
