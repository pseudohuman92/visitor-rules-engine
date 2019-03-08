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
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;

/**
 *
 * @author pseudo
 */
public class RI01 extends Item {
    
    public RI01 (String owner){
        super("RI01", 2, new Hashmap(RED, 1), 
                "1, Activate: \n" +
                "  Opponent purges 2.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 1);
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.spendEnergy(controller, 1);
        game.addToStack(new Activation (controller,
            game.getOpponentName(controller) + " purges 2.",
            (g, cx) -> {
                g.purge(g.getOpponentName(controller), 2);
            }));
    }
}
