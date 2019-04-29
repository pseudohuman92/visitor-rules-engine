/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.red;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 *
 * @author pseudo
 */
public class RI03 extends Item {
    
    public RI03 (String owner){
        super("RI03", 2, new Hashmap(RED, 2), 
            "Charge 3. \n" +
            "\n" +
            "Discharge 1, Activate: \n" +
            "  Opponent purges 3", owner);
    }
    
    @Override
    public void resolve (Game game) {
        super.resolve(game);
        addCounters(CHARGE, 3);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && counters.getOrDefault(CHARGE, 0) > 0;
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        removeCounters(CHARGE, 1);
        game.addToStack(new Activation (controller,
            game.getOpponentName(controller)+" purges 3.",
            (x) -> {
                game.purge(game.getOpponentName(controller), 3);
            })
        );
    }
}
