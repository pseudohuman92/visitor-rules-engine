/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.red;

import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Arraylist;
import com.ccg.ancientaliens.helpers.Hashmap;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class RA03 extends Spell {

    UUID target; 
    
    public RA03(String owner) {
        super("RA03", 1, new Hashmap(RED, 2), 
                "Opponent purges 2. \n" +
                "Shuffle ~ to your deck.", owner);
    }
    
    @Override
    public void resolve (Game game){
        game.purge(game.getOpponentName(controller), 2);
        game.shuffleIntoDeck(controller, new Arraylist<>(this));
    }    
}
