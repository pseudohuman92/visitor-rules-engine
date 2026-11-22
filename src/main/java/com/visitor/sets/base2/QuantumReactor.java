package com.visitor.sets.base2;

import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Asset;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class QuantumReactor extends Asset {
    public QuantumReactor(Game game, UUID owner) {
        super(game, "Quantum Reactor", 4, new CounterMap<>(BLUE, 4),
                "At the start of your turn, gain 1 energy this turn for every 2 Blue knowledge you have, up to 3 energy. "
                        + "Whenever you play a card with Research, draw a card.", owner);
        triggering.addEventChecker(EventChecker.startOfTurnChecker(game, this, true,
                () -> game.addEnergy(controller,
                        Math.min(3, game.getKnowledgeCount(controller, BLUE) / 2), true)));
        triggering.addEventChecker(EventChecker.playChecker(game, this,
                card -> card.controller.equals(controller) && card.getResearchRequirement() > 0,
                card -> game.draw(controller, 1)));
    }
}
