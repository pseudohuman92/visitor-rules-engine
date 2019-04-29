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
            (x) -> {
                game.purge(game.getOpponentName(controller), 2);
            }));
    }
}
