/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;


import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI02 extends Item {
    
    UUID target;
    
    public BI02 (String owner){
        super("BI02", 4, new Hashmap(BLACK, 3), 
        "Sacrifice an item, Activate:\n" +
        "Deal 2 damage to your opponent.\n" +
        "If sacrificed item belongs to him, deal 3 damage instead.", owner);
        subtypes.add("Weapon");
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasInstancesIn(controller, Item.class, "play", 1)&&(!depleted);
    }

    @Override
    public void activate(Game game) {
        target = game.selectFromZone(controller, "play", c->{return c instanceof Item;}, 1, false).get(0);
        game.deplete(id);
        game.destroy(target);
        game.addToStack(new Activation(controller, "deal " + (game.ownedByOpponent(target)? 3:2) +  
            " damage to " + game.getOpponentName(controller),
            (x) -> { game.dealDamage(target, (game.ownedByOpponent(target)?3:2)); }));
    }
}
