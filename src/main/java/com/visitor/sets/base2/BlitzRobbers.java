/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base2;

import com.visitor.card.types.Unit;
import com.visitor.card.containers.EventChecker;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Blitz;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class BlitzRobbers extends Unit {

    public BlitzRobbers(Game game, UUID owner) {
        super(game, "Blitz Robbers",
                1, new CounterMap(RED, 2),
                "Fast\nWhen {~} deals attack damage to a player, draw a card.",
                1, 1,
                owner, Blitz);
        playable.setFast();
        triggering.addEventChecker(EventChecker.dealCombatDamageChecker(game, this, (c, tid, d) -> game.isPlayer(tid), (c, tid, d) ->  game.draw(controller, 1)));
    }
}
