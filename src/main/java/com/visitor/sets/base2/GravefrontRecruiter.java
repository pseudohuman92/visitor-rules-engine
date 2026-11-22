package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Unit;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.sets.token.UnitToken;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Blitz;
import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class GravefrontRecruiter extends Unit {
    public GravefrontRecruiter(Game game, UUID owner) {
        super(game, "Gravefront Recruiter", 2, new CounterMap<>(RED, 1).add(PURPLE, 2),
                "Whenever an ally unit with Blitz dies, create a Bat.", 2, 2, owner);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Death,
                e -> Predicates.isAllyUnit(controller).test((Card) e.data.get(0)) && e.hadCombatAbility(Blitz),
                e -> UnitToken.Bat_1_1(game, controller).resolve()));
    }
}
