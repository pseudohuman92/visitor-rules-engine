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
public class BI06 extends Item {
    
    public BI06 (String owner){
        super("BI06", 1, new Hashmap(BLACK, 1), 
                "Condition - Control a card you don't own in play. Activate: Opponent purges 3.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.controlsUnownedCard(controller, "play");
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.addToStack(new Activation (controller,
            game.getOpponentName(controller) + " purges 3",
            (x) -> {
                game.purge(game.getOpponentName(controller), 3);
            }));
    }
}
