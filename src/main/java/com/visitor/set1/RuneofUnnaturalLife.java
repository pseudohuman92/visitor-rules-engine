/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

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
public class RuneofUnnaturalLife extends Item {
    
    public RuneofUnnaturalLife (String owner){
        super("Rune of Unnatural Life", 3, new Hashmap(BLACK, 1), 
                "Activate, Sacrifice ~:\n" +
"Draw a card from your scrapyard then\n" +
"discard a card.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasInstancesIn(controller, Card.class, "void", 1);
    }

    @Override
    public void activate(Game game) {
        Arraylist<UUID> selected = game.selectFromZone(controller, "scrapyard", c->{return true;}, 1, false);
        game.destroy(id);
        game.addToStack(new Activation (controller,
            "Draw a card from your scrapyard then discard a card.",
            (x) -> {
                if (game.isIn(controller, selected.get(0), "scrapyard")){
                    game.drawByID(controller, selected.get(0));
                    game.discard(controller, 1);
                }
            }, selected));
    }
}
