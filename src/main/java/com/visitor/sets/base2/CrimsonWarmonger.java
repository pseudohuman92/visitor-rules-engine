package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Unit;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Blitz;
import static com.visitor.card.properties.Damagable.CombatAbility.Trample;
import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class CrimsonWarmonger extends Unit {
    public CrimsonWarmonger(Game game, UUID owner) {
        super(game, "Crimson Warmonger", 6, new CounterMap<>(RED, 3).add(PURPLE, 2),
                "Whenever another ally unit dies, deal 1 damage to your opponent. "
                        + "When Crimson Warmonger dies, create 3 Stormtroopers.", 5, 5, owner, Blitz, Trample);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Death,
                e -> Predicates.isAllyUnit(controller).test((Card) e.data.get(0))
                        && !((Card) e.data.get(0)).getId().equals(getId()),
                e -> game.dealDamage(getId(), game.getOpponentId(controller), 1)));
        triggering.addEventChecker(EventChecker.selfDeathChecker(game, this, () -> {
            for (int i = 0; i < 3; i++) new Stormtrooper(game, controller).resolve();
        }));
    }
}
