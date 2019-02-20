/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import helpers.Hashmap;


/**
 *
 * @author pseudo
 */
public class BI01 extends Item {
    
    public BI01 (String owner){
        super("BI01", 1, new Hashmap(BLACK, 1), 
                "Activate: Loot 1", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return (game.players.get(controller).energy > 0)&&(!depleted);
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.addToStack(new Activation("", controller, "Loot 1", null, 
                (g , c) -> { g.loot(controller, 1); }));
    }
}
