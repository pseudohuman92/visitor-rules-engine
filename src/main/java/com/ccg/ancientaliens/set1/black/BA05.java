/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA05 extends Action {
    
    /**
     *
     * @param owner
     */
    public BA05(String owner) {
        super("BA05", 1, new Hashmap(BLACK, 1), 
        "Opponent discards an action.", owner);
    }
    
    @Override
    public void resolve (Game game){
        //This needs to be UpTo
        Arraylist<UUID> selected = game.selectFromZone(game.getOpponentName(controller), "hand", c->{return c instanceof Action;}, 1, true);
        if(!selected.isEmpty()){
            game.discard(game.getOpponentName(controller), selected.get(0));
            game.putTo(controller, this, "scrapyard");
        }
    }
    
}
