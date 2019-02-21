/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA01 extends Action {

    UUID target; 
    
    public YA01(String owner) {
        super("YA01", 2, new Hashmap(YELLOW, 3), 
                "Gain reflect 2.\n" +
                "Shuffle ~ back into your deck.", owner);
    }
    
    @Override
    public void resolve (Game game){
        game.addReflect(controller, 2);
        ArrayList<Card> tmp = new ArrayList<>();
        tmp.add(this);
        game.shuffleIntoDeck(controller, tmp);
    }    
}
