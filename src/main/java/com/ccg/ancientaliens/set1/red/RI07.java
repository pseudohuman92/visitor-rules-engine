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
public class RI07 extends Item {
    
    public RI07 (String owner){
        super("RI07", 1, new Hashmap(RED, 2), 
                "X, Purge X, Activate: \n" +
                "  Opponent purge X", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 1);
    }
    
    @Override
    public void activate(Game game) {
        int x = game.selectX(controller, Math.min(game.getEnergy(controller), game.getZone(controller, "deck").size()));
        game.purgeSelf(controller, x);
        game.spendEnergy(controller, x);
        game.deplete(id);
        game.addToStack(new Activation(controller, game.getOpponentName(controller)+" purges " + x,
        (g, c) -> {
            g.purge(g.getOpponentName(controller), x);
        }));
    }
}
