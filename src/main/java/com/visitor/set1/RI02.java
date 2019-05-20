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
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class RI02 extends Item {
    
    UUID target;
    
    public RI02 (String owner){
        super("RI02", 1, new Hashmap(RED, 2), 
            "Activate: \n" +
            "  Return ~ and target item to controller's hand.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasValidTargetsIn(controller, c->{return (c instanceof Item && !c.id.equals(id));}, 1, "both play");
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        target = game.selectFromZone(controller, "both play", c->{return (c instanceof Item && !c.id.equals(id));}, 1, false).get(0);
        game.addToStack(new Activation (controller,
            "Return ~ and target item to controller's hand.",
            (x) -> {
                Card c = game.extractCard(target);
                game.putTo(controller, game.extractCard(id), "hand");
                game.putTo(c.controller, c, "hand");
            }, new Arraylist<>(target).putIn(id))
        );
    }
}
