/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI04 extends Item {
    
    public BI04 (String owner){
        super("BI04", 3, new Hashmap(BLACK, 1), 
                "Activate, Destroy Portable Gateway: Draw a card from your void, then purge a card from your hand.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasInstancesIn(controller, Card.class, "void", 1);
    }

    @Override
    public void activate(Game game) {
        Arraylist<UUID> selected = game.selectFromZone(controller, "void", c->{return true;}, 1, false);
        game.destroy(id);
        game.addToStack(new Activation (controller,
            "Draw a card from void, then purge acard from your hand",
            (g, c) -> {
                if (g.isIn(c.controller, selected.get(0), "void")){
                    g.drawByID(c.controller, selected.get(0));
                    Arraylist<UUID> selection = g.selectFromZone(c.controller, "hand", cx-> {return true;}, 1, false);
                    g.purgeByID(c.controller, selection.get(0));
                }
            }));
    }
}
