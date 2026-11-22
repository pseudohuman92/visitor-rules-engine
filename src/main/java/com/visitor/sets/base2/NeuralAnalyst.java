package com.visitor.sets.base2;

import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Unit;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class NeuralAnalyst extends Unit {
    public NeuralAnalyst(Game game, UUID owner) {
        super(game, "Neural Analyst", 3, new CounterMap<>(BLUE, 2),
                "Whenever you gain knowledge, Neural Analyst gains +1/+1 until end of turn.", 2, 3, owner);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Knowledge_Gained,
                e -> controller.equals(e.data.get(0)),
                e -> game.addAttackAndHealth(getId(), 1, 1, true)));
    }
}
