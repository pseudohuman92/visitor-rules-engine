/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class UA04 extends Action {
    
    /**
     *
     * @param owner
     */
    public UA04(String owner) {
        super("UA04", 2, new Hashmap(BLUE, 2), 
        "Choose an item from opponent's hand and transform it into Junk", owner);
    }
    
    @Override
    public void resolve (Game game){
        ArrayList <UUID> canSelected = new ArrayList<>();
        game.getZone(game.getOpponentName(controller), "hand").forEach(c -> {
            if (c instanceof Item){
                canSelected.add(c.id);
            }
        });
        ArrayList<UUID> s = game.selectFromListUpTo(controller, game.getZone(game.getOpponentName(controller), "hand"), canSelected, 1);
        if (!s.isEmpty()){
            game.transformToJunk(s.get(0));
        }
        game.putTo(controller, this, "scrapyard");
    }
    
}
