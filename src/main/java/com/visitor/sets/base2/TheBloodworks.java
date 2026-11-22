package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Asset;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class TheBloodworks extends Asset {
    private boolean createdThisTurn;

    public TheBloodworks(Game game, UUID owner) {
        super(game, "The Bloodworks", 3, new CounterMap<>(RED, 1).add(PURPLE, 2),
                "Once each turn, when an ally unit dies, create a Bloodrunner. "
                        + "Whenever you sacrifice an ally unit, deal 1 damage to your opponent.", owner);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Turn_Start,
                e -> true, e -> createdThisTurn = false));
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Death,
                e -> !createdThisTurn && Predicates.isAllyUnit(controller).test((Card) e.data.get(0)),
                e -> {
                    createdThisTurn = true;
                    new Bloodrunner(game, controller).resolve();
                }));
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Sacrifice,
                e -> Predicates.isAllyUnit(controller).test((Card) e.data.get(0)),
                e -> game.dealDamage(getId(), game.getOpponentId(controller), 1)));
    }
}
