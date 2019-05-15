/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.additional;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.set1.blue.UI01;
import com.visitor.helpers.Hashmap;


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
                game.getOpponentName(controller) + " purges 6",
                (x) -> { game.damagePlayer(game.getOpponentName(controller), 6); }));    }
}
