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
import static com.ccg.ancientaliens.protocol.Types.Counter.CHARGE;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class YI06 extends Item {
    
    public YI06 (String owner){
        super("YI06", 2, new Hashmap(YELLOW, 2), 
                "\"Sacrifice an item, Activate:\n" +
                "  Opponent purges X, \n" +
                "  where X is # of charge counter on sacrificed item.\"", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasInstancesIn(controller, Item.class, "play", 1);
    }
    
    @Override
    public void activate(Game game) {
        UUID selection = game.selectFromZone(controller, "play", c->{return c instanceof Item;}, 1, false).get(0);
        int x = game.getCard(selection).counters.getOrDefault(CHARGE, 0);
        game.destroy(selection);
        game.deplete(id);
        game.addToStack(new Activation(controller, game.getOpponentName(controller)+" purges " + x,
        (y) -> {
            game.purge(game.getOpponentName(controller), x);
        }));
    }
}
