/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.additional;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.card.types.Junk;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import com.ccg.ancientaliens.set1.blue.UI04;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class AI02 extends Item {
    
    public AI02 (UI04 c){
        super("AI02", 3, new Hashmap(BLUE, 3), 
                "Purge a Junk from your hand: Target opponent Purges 5", c.controller);
        copyPropertiesFrom(c);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasInstancesIn(controller, Junk.class, "hand", 1);
    }
    
    @Override
    public void activate(Game game) {
        Arraylist<UUID> selected = game.selectFromZone(controller, "hand", c -> {return c instanceof Junk;}, 1, false);
        game.purgeByID(controller, selected.get(0));
        game.addToStack(new Activation(controller, 
                "Opponent purges 5",
                (g , c) -> { g.purge(g.getOpponentName(c.controller), 5); }));    }
}
