/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class UA04 extends Spell {
    
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
        Arraylist<UUID> s = game.selectFromList(controller, 
                game.getZone(game.getOpponentName(controller), "hand"), 
                c->{return c instanceof Item;}, 1, true);
        if (!s.isEmpty()){
            game.transformToJunk(s.get(0));
        }
        game.putTo(controller, this, "scrapyard");
    }
    
}
