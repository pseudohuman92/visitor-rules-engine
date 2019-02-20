/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.properties.Targeting;
import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI04 extends Item implements Targeting {
    
    public BI04 (String owner){
        super("BI04", 3, new Hashmap(BLACK, 1), 
                "Activate, Destroy Portable Gateway: Draw a card from your void, then purge a card from your hand.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasAnInstanceIn(controller, Card.class, "void");
    }

    @Override
    public void activate(Game game) {
        ArrayList<UUID> selected = game.getSelectedFromVoid(controller, this::validTarget, 1);
        game.destroy(id);
        game.addToStack(new Activation ("", controller,
            "Draw a card from void, then purge acard from your hand",
            null, (g, c) -> {
                if (g.isIn(c.controller, selected.get(0), "void")){
                    g.drawByID(c.controller, selected.get(0));
                    ArrayList<UUID> selection = g.getSelectedFromHand(c.controller, (cx-> {return true;}), 1);
                    g.purgeByID(c.controller, selection.get(0));
                }
            }));
    }

    @Override
    public boolean validTarget(Card c) {
        return c.controller.equals(controller);
    }
}
