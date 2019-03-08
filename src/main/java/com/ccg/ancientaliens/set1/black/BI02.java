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
import com.ccg.ancientaliens.helpers.Arraylist;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI02 extends Item implements Targeting {
    
    UUID target;
    
    public BI02 (String owner){
        super("BI02", 4, new Hashmap(BLACK, 3), 
        "Sacrifice an item, Activate: Opponent purges 5. If sacrificed item belongs to him, he purges 10 instead.", owner);
        subtypes.add("Weapon");
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasInstancesIn(controller, Item.class, "play", 1)&&(!depleted);
    }

    @Override
    public void activate(Game game) {
        target = game.selectFromZone(controller, "play", this::validTarget, 1, false).get(0);
        game.deplete(id);
        game.destroy(target);
        String oppName = game.getOpponentName(controller);
        game.addToStack(new Activation(controller, 
            game.getOpponentName(controller) + (game.ownedByOpponent(target)?" purges 10":" purges 5"),
            (g, c) -> { g.purge(oppName, (game.ownedByOpponent(target)?10:5)); }));
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item);
    }
}
