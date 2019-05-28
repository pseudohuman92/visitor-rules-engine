/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import com.visitor.helpers.Hashmap;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class AI03 extends Item {
    
    public AI03 (UI08 c){
        super("AI03", 0, new Hashmap(BLUE, 1), 
                "Discharge 1, Activate: \n" +
                "  Deal 2 damage. \n" +
                "  If ~ has no counters on it, transform ~ to UI08", c.controller);
        copyPropertiesFrom(c);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && counters.get(CHARGE) > 0;
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        removeCounters(CHARGE, 1);
        UUID target = game.selectDamageTargets(controller, 1, false).get(0);
        game.addToStack(new Activation(controller, 
                "Deal 2 damage\n" +
                "If ~ has no counters on it, transform ~ to UI08",
            (x) -> {
                game.dealDamage(id, target, 2);
                if (counters.get(CHARGE) == 0){
                    game.replaceWith(this, new UI08(this));
                }
            },  new Arraylist(target).putIn(id)));
    }
}
