/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.additional;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.set1.blue.UI01;
import helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class AI01 extends Item {
    
    public AI01 (UI01 c){
        super("AI01", 1, new Hashmap(), 
                "Sacrifice ~: Opponent purges 6.", c.controller);
        copyPropertiesFrom(c);
    }

    @Override
    public boolean canActivate(Game game) {
        return true;
    }
    
    @Override
    public void activate(Game game) {
        game.destroy(id);
        game.addToStack(new Activation(controller, 
                "Opponent purges 6",
                (g , c) -> { g.purge(g.getOpponentName(c.controller), 6); }));    }
}
