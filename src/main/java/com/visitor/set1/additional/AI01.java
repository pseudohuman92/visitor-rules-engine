/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.additional;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.set1.blue.UI01;
import com.visitor.helpers.Hashmap;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class AI01 extends Item {
    
    UUID target;
    
    public AI01 (UI01 c){
        super("AI01", 1, new Hashmap(), 
                "Sacrifice ~: Deal 5 damage.", c.controller);
        copyPropertiesFrom(c);
    }

    @Override
    public boolean canActivate(Game game) {
        return true;
    }
    
    @Override
    public void activate(Game game) {
        game.destroy(id);
        target = game.selectDamageTargets(controller, 1, false).get(0);
        game.addToStack(new Activation(controller, "Deal 5 damage",
        (y) -> {
            game.dealDamage(target, 5);
        }, new Arraylist(target).putIn(id)));
    }
}
