/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base2;

import com.visitor.card.properties.Damagable;
import com.visitor.card.types.Unit;
import com.visitor.card.containers.EventChecker;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.sets.token.UnitToken;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.*;

/**
 * @author pseudo
 */
public class Vampireling extends Unit {

    public Vampireling(Game game, UUID owner) {
        super(game, "Vampireling",
                2, new CounterMap(PURPLE, 1),
                "When {~} dies, create a Bat.",
                2, 2,
                owner, Damagable.CombatAbility.Drain, Damagable.CombatAbility.Evasive);
        triggering.addEventChecker(EventChecker.selfDeathChecker(game, this, () -> {
                UnitToken.Bat_1_1(game, controller).resolve();
        }));
    }
}
