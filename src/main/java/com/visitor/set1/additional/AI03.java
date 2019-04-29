/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.additional;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import com.visitor.set1.blue.UI08;
import com.visitor.helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class AI03 extends Item {
    
    public AI03 (UI08 c){
        super("AI03", 0, new Hashmap(BLUE, 1), 
                "Discharge 1: \n" +
                "  Opponent purges 4. \n" +
                "  If ~ has no counters on it, transform ~ to UI08", c.controller);
        copyPropertiesFrom(c);
    }

    @Override
    public boolean canActivate(Game game) {
        return counters.get(CHARGE) > 0;
    }
    
    @Override
    public void activate(Game game) {
        counters.putIn(CHARGE, counters.get(CHARGE) - 1);
        game.addToStack(new Activation(controller, 
                game.getOpponentName(controller) + " purges 4. \n" +
                "If ~ has no counters on it, transform ~ to UI08",
            (x) -> {
                game.purge(game.getOpponentName(controller), 4);
                if (counters.get(CHARGE) == 0){
                    game.replaceWith(this, new UI08(this));
                }
            }));
    }
}
