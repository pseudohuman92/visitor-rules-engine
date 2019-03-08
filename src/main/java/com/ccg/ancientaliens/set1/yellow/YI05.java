/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import com.ccg.ancientaliens.helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class YI05 extends Item {
    
    public YI05 (String owner){
        super("YI05", 2, new Hashmap(YELLOW, 2), 
                "\"2, Activate:\n" +
                "  Gain reflect 2.\"", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 2);
    }
    
    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 2);
        game.addToStack(new Activation(controller, controller+" gains reflect 2",
        (g, c) -> {
            game.addReflect(controller, 2);
        }));
    }
}
