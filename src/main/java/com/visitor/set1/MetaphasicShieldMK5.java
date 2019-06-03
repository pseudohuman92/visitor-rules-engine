/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.YELLOW;


/**
 *
 * @author pseudo
 */
public class MetaphasicShieldMK5 extends Item {
    
    public MetaphasicShieldMK5 (String owner){
        super("Metaphasic Shield MK 5", 1, new Hashmap(YELLOW, 1), 
                "Charge 4.\n" +
                "3, Sacrifice ~: \n" +
                "  Gain shield X.\n" +
                "  X = # of charge counters\"", owner);
        subtypes.add("Barrier");
    }
    
    @Override
    public void resolve(Game game) {
        super.resolve(game);
        addCounters(CHARGE, 4);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasEnergy(controller, 3);
    }
    
    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 3);
        int x = counters.get(CHARGE);
        counters.removeFrom(CHARGE);
        game.sacrifice(id);
        
        game.addToStack(new Ability(this, controller+" gains shield " + x,
            (y) -> { 
                game.addShield(controller, x);
        }));
    }
}
