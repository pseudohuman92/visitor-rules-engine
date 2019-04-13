/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI03 extends Item {
    
    UUID target;
    
    public BI03 (String owner){
        super("BI03", 4, new Hashmap(BLACK, 2), 
        "Sacrifice an Item: Gain 1 Energy. If that item is owned by the opponent gain 1 additional energy.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasInstancesIn(controller, Item.class, "play", 1);
    }

    @Override
    public void activate(Game game) {
        target = game.selectFromZone(controller, "play", c->{return c instanceof Item;}, 1, false).get(0);
        game.destroy(target);
        game.addToStack(new Activation(controller, 
            controller + " gains " + (game.ownedByOpponent(target)?2:1) + " energy",
            (x) -> { game.addEnergy(controller, (game.ownedByOpponent(target)?2:1)); }));
    }
}
