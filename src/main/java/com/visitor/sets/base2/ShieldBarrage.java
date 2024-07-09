/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base2;

import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.YELLOW;

/**
 * @author pseudo
 */
public class ShieldBarrage extends Cantrip {

    int shield_amount = 0;
    public ShieldBarrage(Game game, UUID owner) {
        super(game, "Shield Barrage",
                1, new CounterMap(YELLOW, 1),
                "Remove all shields from target ally unit and deal damage to any enemy equal to the removed amount.",
                owner);
        playable.addTargetSingleUnit(Base.Zone.Play, Predicates.isAllyUnit(controller), t -> {
            shield_amount = game.getCard(t).getShields();
            game.getCard(t).removeShields(shield_amount);
        }, "Remove all shields from ally unit.", false);
        playable.addTargetSingleUnit(Base.Zone.Both_Play, Predicates.isEnemy(controller), t -> game.dealDamage(getId(), t, shield_amount),
    "Choose an enemy target.", false );
    }
}
