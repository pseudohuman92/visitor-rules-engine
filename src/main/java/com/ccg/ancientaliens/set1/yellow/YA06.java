/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import com.ccg.ancientaliens.helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public class YA06 extends Action {

    public YA06(String owner) {
        super("YA06", 3, new Hashmap(YELLOW, 2), 
                "Each player draws 3 cards", owner);
    }
    
    @Override
    public void resolve (Game game){
        game.draw(controller, 3);
        game.draw(game.getOpponentName(controller), 3);
    }    
}
