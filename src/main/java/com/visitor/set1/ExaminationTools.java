/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Asset;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.GREEN;


/**
 *
 * @author pseudo
 */
public class ExaminationTools extends Asset {
    
    public ExaminationTools (String owner){
        super("Examination Tools", 2, new Hashmap(GREEN, 2), 
                "1, Pay 3 life, Activate: \n" +
                "    You can study one additional time.", 
                1,
                owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game) &&
               game.hasEnergy(controller, 1);
    }
    
    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 1);
        game.payLife(controller, 3);
        game.deplete(id);
        game.addToStack(new Ability(this, 
                "You can study one additional time.",
            (x) -> {
                game.addStudyCount(controller, 1);
            }));
    }
}
