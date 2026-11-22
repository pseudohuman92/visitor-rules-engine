package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Unit;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Trample;
import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class AshenReclaimer extends Unit {
    public AshenReclaimer(Game game, UUID owner) {
        super(game, "Ashen Reclaimer", 4, new CounterMap<>(RED, 2).add(PURPLE, 1),
                "Whenever another ally unit dies, Ashen Reclaimer gains +1/+0 until end of turn. "
                        + "Whenever Ashen Reclaimer attacks, it gains Trample until end of turn.", 3, 4, owner);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Death,
                e -> Predicates.isAllyUnit(controller).test((Card) e.data.get(0))
                        && !((Card) e.data.get(0)).getId().equals(getId()),
                e -> game.addAttackAndHealth(getId(), 1, 0, true)));
        triggering.addEventChecker(EventChecker.attackChecker(game, this,
                attackers -> attackers.stream().anyMatch(c -> c.getId().equals(getId())),
                attackers -> game.addCombatAbility(getId(), Trample, true)));
    }
}
