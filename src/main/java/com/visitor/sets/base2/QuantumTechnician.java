package com.visitor.sets.base2;

import com.visitor.card.containers.ActivatedAbility;
import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class QuantumTechnician extends Unit {
    public QuantumTechnician(Game game, UUID owner) {
        super(game, "Quantum Technician", 3, new CounterMap<>(BLUE, 3),
                "{Use}: Gain 1 energy this turn. {B}{B}{B} — Gain 2 energy instead.", 2, 4, owner);
        activatable.addActivatedAbility(new ActivatedAbility(game, this, 0, text,
                () -> game.addEnergy(controller, game.getKnowledgeCount(controller, BLUE) >= 3 ? 2 : 1, true))
                .setDepleting());
    }
}
