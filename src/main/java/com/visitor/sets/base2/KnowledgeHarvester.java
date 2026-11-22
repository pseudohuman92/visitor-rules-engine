package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Unit;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class KnowledgeHarvester extends Unit {
    public KnowledgeHarvester(Game game, UUID owner) {
        super(game, "Knowledge Harvester", 2, new CounterMap<>(BLUE, 2),
                "Whenever you study a Tome, gain 1 energy this turn.", 1, 3, owner);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Study,
                e -> controller.equals(e.data.get(0)) && ((Card) e.data.get(1)).hasType(Card.CardType.Tome),
                e -> game.addEnergy(controller, 1, true)));
    }
}
