/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class Meltdown extends Item {
    
    UUID target;
    
    public Meltdown (SelfdestructNode c){
        super("Meltdown", 1, new Hashmap(), 
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
        game.addToStack(new Ability(this, "Deal 5 damage",
        (y) -> {
            game.dealDamage(id, target, 5);
        }, new Arraylist<>(target)));
    }
}
