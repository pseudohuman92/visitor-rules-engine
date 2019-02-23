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
        UUID selected = game.selectFromHand(game.getOpponentName(controller), c->{return c instanceof Action;}, 1).get(0);
        game.discard(game.getOpponentName(controller), selected);
        game.putTo(controller, this, "scrapyard");
    }
    
}
