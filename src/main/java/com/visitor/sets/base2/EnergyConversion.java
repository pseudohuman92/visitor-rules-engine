package com.visitor.sets.base2;

import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class EnergyConversion extends Cantrip {
    public EnergyConversion(Game game, UUID owner) {
        super(game, "Energy Conversion", 2, new CounterMap<>(BLUE, 2),
                "Gain 1 energy this turn for each Blue knowledge you have, up to 4.", owner);
        playable.addResolveEffect(() -> game.addEnergy(controller,
                Math.min(4, game.getKnowledgeCount(controller, BLUE)), true));
    }
}
