package com.visitor.sets.base2;

import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Blitz;
import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class Bloodrunner extends Unit {
    public Bloodrunner(Game game, UUID owner) {
        super(game, "Bloodrunner", 1, new CounterMap<>(RED, 1).add(PURPLE, 1),
                "When Bloodrunner dies, deal 1 damage to your opponent.", 2, 1, owner, Blitz);
        triggering.addEventChecker(EventChecker.selfDeathChecker(game, this,
                () -> game.dealDamage(getId(), game.getOpponentId(controller), 1)));
    }
}
