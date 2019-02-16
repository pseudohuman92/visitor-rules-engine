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
public class BI03 extends Item implements Targeting {
    
    public BI03 (String owner){
        super("BI03", 4, new Hashmap(BLACK, 2), 
                "Sacrifice an Item: Gain 1 Energy. If that item is owned by the opponent gain 1 additional energy.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.players.get(controller).hasAnItem();
    }

    @Override
    public void activate(Game game) {
        game.getTargetsFromPlay(this, 1);
        UUID sacID = ((UUID[])supplementaryData)[0];
        game.destroy(sacID);
        if (game.ownedByOpponent(sacID)) {
            game.addToStack(new Activation("", controller, 
                "Gain 2 energy", game.players.get(controller).id,
                g -> { g.addEnergy(controller, 2); }));
        } else {
            game.addToStack(new Activation("", controller, 
                "Gain 2 energy", game.players.get(controller).id,
                g -> { g.addEnergy(controller, 2); }));
        }
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item && c.controller.equals(controller));
    }
}
