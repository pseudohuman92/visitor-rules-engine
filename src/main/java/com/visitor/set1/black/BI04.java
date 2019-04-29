/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.black;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI04 extends Item {
    
    public BI04 (String owner){
        super("BI04", 3, new Hashmap(BLACK, 1), 
                "Activate, Sacrifice ~: Draw a card from your void, then purge a card from your hand.", owner);
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
            (x) -> {
                if (game.isIn(controller, selected.get(0), "void")){
                    game.drawByID(controller, selected.get(0));
                    Arraylist<UUID> selection = game.selectFromZone(controller, "hand", cx-> {return true;}, 1, false);
                    game.purgeByID(controller, selection.get(0));
                }
            }, selected));
    }
}
