/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.GREEN;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class EnergyCannon extends Item {
    
    int x;
    UUID target;
    
    public EnergyCannon (String owner){
        super("Energy Cannon", 7, new Hashmap(GREEN, 4), 
                "X, Activate: \n" +
                "    Deal X damage.", 
                6,
                owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game);
    }
    
    @Override
    public void activate(Game game) {
        x = game.selectX(controller, game.getEnergy(controller));
        target = game.selectDamageTargets(controller, 1, false).get(0);
        game.spendEnergy(controller, x);
        game.deplete(id);
        game.addToStack(new Ability(this, 
                "Deal "+x+" damage",
            (a) -> {
                game.dealDamage(id, target, x);
            }, target));
    }
}
