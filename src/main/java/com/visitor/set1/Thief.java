/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class Thief extends Item {
    
    public Thief (String owner){
        super("Thief", 1, new Hashmap(BLACK, 1), 
                "Activate: Loot 1", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.isActive(controller) && game.hasEnergy(controller, 1)&&(!depleted);
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.addToStack(new Ability(this, controller + " loots 1", 
                (x) -> { game.loot(controller, 1); }));
    }
}
