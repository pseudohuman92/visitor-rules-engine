/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base2;

import com.visitor.card.containers.EventChecker;
import com.visitor.card.properties.Damagable;
import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.sets.token.UnitToken;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class ZombieAmalgam extends Unit {

    public ZombieAmalgam(Game game, UUID owner) {
        super(game, "Zombie Amalgam",
                3, new CounterMap(PURPLE, 2),
                "When {~} dies, create 2 Zombies with Decay.",
                5, 5,
                owner, Damagable.CombatAbility.Trample, Damagable.CombatAbility.Decay);
        triggering.addEventChecker(EventChecker.selfDeathChecker(game, this, ()->
        {
            UnitToken.Zombie_2_2(game, controller).resolve();
            UnitToken.Zombie_2_2(game, controller).resolve();
        }));
    }
}
