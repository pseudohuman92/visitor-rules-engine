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
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI02 extends Item implements Targeting {
    
    public BI02 (String owner){
        super("BI02", 4, new Hashmap(BLACK, 3), 
        "Sacrifice an item, Activate: Opponent purges 5. If sacrificed item belongs to him, he purges 10 instead.", owner);
        subtypes.add("Weapon");
    }

    @Override
    public boolean canActivate(Game game) {
        return (!game.players.get(controller).hasInPlay(Item.class))&&(!depleted);
    }

    @Override
    public void activate(Game game) {
        game.getSelectedFromPlay(controller, this::validTarget, 1);
        game.deplete(id);
        UUID targetID = supplementaryData.get(0);
        game.destroy(targetID);
        String oppName = game.getOpponentName(controller);
        if (game.ownedByOpponent(targetID)) {
            game.addToStack(new Activation("", controller, 
                "Purge 10", null,
                (g, c) -> { g.purge(oppName, 10); }));
        } else {
            game.addToStack(new Activation("", controller, 
                "Purge 5", null,
                (g, c) -> { g.purge(oppName, 5); }));
        }
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item && c.controller.equals(controller));
    }
}
