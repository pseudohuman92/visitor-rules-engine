/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.red;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Hashmap;
import static com.ccg.ancientaliens.protocol.Types.Counter.CHARGE;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;

/**
 *
 * @author pseudo
 */
public class RI03 extends Item {
    
    public RI03 (String owner){
        super("RI03", 2, new Hashmap(RED, 2), 
            "Charge 3. \n" +
            "\n" +
            "Discharge 1, Activate: \n" +
            "  Opponent purges 3", owner);
    }
    
    @Override
    public void resolve (Game game) {
        super.resolve(game);
        counters.put(CHARGE, 3);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && counters.getOrDefault(CHARGE, 0) > 0;
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        removeCounters(CHARGE, 1);
        game.addToStack(new Activation (controller,
            "Opponent purges 3.",
            (g, cx) -> {
                g.purge(g.getOpponentName(controller), 3);
            })
        );
    }
}
