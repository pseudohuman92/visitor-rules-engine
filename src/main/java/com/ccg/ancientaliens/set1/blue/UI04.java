/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.properties.Transforming;
import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import com.ccg.ancientaliens.set1.additional.AI02;
import helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class UI04 extends Item implements Transforming {
    
    public UI04 (String owner){
        super("UI04", 1, new Hashmap(BLUE, 1), 
                "1, Discard 1: Transform ~ into AI02.", owner);
        subtypes.add("Kit");
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasACardIn(controller, "hand")
                && game.hasEnergy(controller, 1);
    }
    
    @Override
    public void activate(Game game) {
        game.discard(controller, 1);
        game.spendEnergy(controller, 1);
        game.addToStack(new Activation(controller, "Transform ~ into AI02.",
            (g , c) -> { 
                if(g.isIn(controller, id, "play"))
                    transform(g); 
        }));
    }

    @Override
    public void transform(Game game) {
        game.replaceWith(this, new AI02(this));
    }
}
