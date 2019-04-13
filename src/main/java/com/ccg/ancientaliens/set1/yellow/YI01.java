/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Counter.CHARGE;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import com.ccg.ancientaliens.helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class YI01 extends Item {
    
    public YI01 (String owner){
        super("YI01", 1, new Hashmap(YELLOW, 1), 
                "Charge 4.\n" +
                "\n" +
                "3, Sacrifice ~: \n" +
                "  Gain shield X.\n" +
                "  X = # of charge counters\"", owner);
        subtypes.add("Barrier");
    }
    
    @Override
    public void resolve(Game game) {
        super.resolve(game);
        addCounters(CHARGE, 4);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasEnergy(controller, 3);
    }
    
    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 3);
        int x = counters.get(CHARGE);
        counters.removeFrom(CHARGE);
        game.destroy(id);
        
        game.addToStack(new Activation(controller, controller+" gains shield " + x,
            (y) -> { 
                game.addShield(controller, x);
        }));
    }
}
