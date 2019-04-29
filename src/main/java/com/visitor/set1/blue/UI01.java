/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.blue;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import com.visitor.set1.additional.AI01;
import com.visitor.helpers.Hashmap;


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
            (x) -> { 
                if(game.isIn(controller, id, "play")) 
                    game.replaceWith(this, new AI01(this));
        }, new Arraylist<>(id)));
    }
}
