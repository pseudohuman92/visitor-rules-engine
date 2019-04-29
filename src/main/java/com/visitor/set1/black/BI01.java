/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.black;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class BI01 extends Item {
    
    public BI01 (String owner){
        super("BI01", 1, new Hashmap(BLACK, 1), 
                "Activate: Loot 1", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.isActive(controller) && game.hasEnergy(controller, 1)&&(!depleted);
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.addToStack(new Activation(controller, controller + " loots 1", 
                (x) -> { game.loot(controller, 1); }));
    }
}
