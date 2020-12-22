/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;


import com.visitor.card.types.Ally;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class MysticGnome extends Ally {

    public MysticGnome(Game game, String owner) {
        super(game,"Mystic Gnome", 1, new CounterMap<>(GREEN, 1),
                "{1}, {D}: +1 Loyalty \n" +
                        "-2 Loyalty, {D}: \n" +
                        "    Delay 1 - Deal X damage to a target. \n" +
                        "    X = Your max energy.",
                2,
                owner);

        addPlusLoyaltyAbility(1, "{1}, {D}: +1 Loyalty", 1, null, null, null);

        //TODO: check targeting to see if it works.
        addMinusLoyaltyAbility(0, "-2 Loyalty, {D}: Delay 1 - Deal X damage to a target.\nX = Your max energy.", 2,1,
		        new ActivatedAbility(game, this, 0, "Deal X damage to a target.\nX = Your max energy.")
				        .setTargetingForDamageDuringResolve(target -> game.dealDamage(id, target, game.getMaxEnergy(controller))), null, null);
    }
}

