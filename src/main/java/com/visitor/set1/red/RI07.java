/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.red;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.RED;


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
        (y) -> {
            game.purge(game.getOpponentName(controller), x);
        }));
    }
}
