/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Activation;
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
public class RI06 extends Item {
    
    public RI06 (String owner){
        super("RI06", 1, new Hashmap(RED, 2), 
                "Sacrifice an Item, Activate: \n" +
                "  Deal X damage. \n" +
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
        UUID target = game.selectDamageTargets(controller, 1, false).get(0);
        game.addToStack(new Activation(this,
            "Deal "+x+" damage",
            (y) -> {
                game.dealDamage(id, target, x);
            }, new Arraylist<>(selection))
        );
    }
}
