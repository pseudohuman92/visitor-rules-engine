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

/**
 *
 * @author pseudo
 */
public class BI05 extends Item {
    
    public BI05 (String owner){
        super("BI05", 3, new Hashmap(BLACK, 2), 
                "3, Activate: Draw top item of opponent's deck.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted
                && game.hasEnergy(controller, 3);
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.spendEnergy(controller, 3);
        game.addToStack(new Activation (controller,
            "Draw top item of " + game.getOpponentName(controller) + "'s deck",
            (x) -> {
                Card c = game.getPlayer(game.getOpponentName(controller))
                        .deck.extractInstanceFromTop(Item.class);
                if(c != null){
                    c.controller = controller;
                    c.knowledge = new Hashmap<>();
                    game.putTo(c.controller, c, "hand");
                }
            }));
    }
}
