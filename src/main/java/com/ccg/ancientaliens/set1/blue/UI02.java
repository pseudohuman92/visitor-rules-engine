/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.card.types.Junk;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;


/**
 *
 * @author pseudo
 */
public class UI02 extends Item {
    
    public UI02 (String owner){
        super("UI02", 2, new Hashmap(BLUE, 1), 
                "Shuffle 3 Junk into your deck, Activate: Opponent purges 3.", owner);
        subtypes.add("Kit");
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted;
    }
    
    @Override
    public void activate(Game game) {
        Arraylist<Card> junks = new Arraylist<>();
        junks.add(new Junk(controller));
        game.shuffleIntoDeck(controller, junks);
        game.deplete(id);
        game.addToStack(new Activation(controller, game.getOpponentName(controller) + " purges 3",
            (g , c) -> { g.purge(g.getOpponentName(controller), 3);
        }));
    }
}
