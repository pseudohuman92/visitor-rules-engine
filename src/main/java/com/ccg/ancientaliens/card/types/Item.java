
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.card.properties.Activatable;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.protocol.Types.Knowledge;
import static com.ccg.ancientaliens.protocol.Types.Phase.MAIN;
import helpers.Hashmap;

/**
 * Abstract class for the Item card type.
 * @author pseudo
 */
public abstract class Item extends Card implements Activatable {
    
    /**
     *
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Item(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, owner);
    }
    
    
    @Override
    public void resolve(Game game) {
        depleted = true;
        game.players.get(controller).playArea.add(this);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return (game.players.get(controller).energy >= cost)
               && game.players.get(controller).hasKnowledge(knowledge)
               && game.turnPlayer.equals(controller)
               && game.stack.isEmpty()
               && game.phase == MAIN;
    }  
}
