/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Arraylist;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import com.ccg.ancientaliens.set1.additional.AI01;
import com.ccg.ancientaliens.helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class UI01 extends Item {
    
    public UI01 (String owner){
        super("UI01", 2, new Hashmap(BLUE, 2), 
                "Discard 2: Transform ~ into AI01.", owner);
        subtypes.add("Kit");
    }

    @Override
    public boolean canActivate(Game game) {
        return game.getZone(controller, "hand").size() >= 2;
    }
    
    @Override
    public void activate(Game game) {
        game.discard(controller, 2);
        game.addToStack(new Activation(controller, "Transform ~ into AI01.",
            (g , c) -> { 
                if(g.isIn(controller, id, "play")) 
                    g.replaceWith(this, new AI01(this));
        }, new Arraylist<>(id)));
    }
}
