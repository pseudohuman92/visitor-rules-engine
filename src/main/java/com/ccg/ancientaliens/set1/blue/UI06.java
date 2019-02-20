/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class UI06 extends Item{
    
    public UI06 (String owner){
        super("UI06", 2, new Hashmap(BLUE, 2), 
                "Discard 1, Purge 1: Gain 1 Energy", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !game.players.get(controller).hand.isEmpty();
    }
    
    @Override
    public void activate(Game game) {
        game.discard(controller, 1);
        game.purge(controller, 1);
        game.addToStack(new Activation("", controller, "Gain 1 energy", null, 
                (g , c) -> { g.addEnergy(c.controller, 1); }));
    }
}
