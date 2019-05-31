/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import com.visitor.helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class KineticReflectors extends Item {
    
    public KineticReflectors (String owner){
        super("Kinetic Reflectors", 2, new Hashmap(YELLOW, 2), 
                "2, Activate:\n" +
                "  Gain reflect 2.", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 2);
    }
    
    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 2);
        game.deplete(id);
        game.addToStack(new Ability(this, controller+" gains reflect 2",
        (x) -> {
            game.addReflect(controller, 2);
        }));
    }
}
