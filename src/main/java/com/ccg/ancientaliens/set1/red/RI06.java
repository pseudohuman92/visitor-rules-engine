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
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class RI06 extends Item {
    
    public RI06 (String owner){
        super("RI06", 1, new Hashmap(RED, 2), 
                "Sacrifice an Item, Activate: \n" +
                "  Opponent purges X. \n" +
                "  X = cost of sacrificed item.", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasInstancesIn(controller, Item.class, "play", 1);
    }
    
    @Override
    public void activate(Game game) {
        UUID selection = game.selectFromZone(controller, "play", c->{return c instanceof Item;}, 1, false).get(0);
        int x = game.getCard(selection).cost;
        game.destroy(selection);
        game.deplete(id);
        game.addToStack(new Activation(controller, "Opponent purges " + x,
        (g, c) -> {
            g.purge(g.getOpponentName(controller), x);
        }));
    }
}
