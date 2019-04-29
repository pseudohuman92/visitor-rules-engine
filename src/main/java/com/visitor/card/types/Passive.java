
package com.visitor.card.types;

import com.visitor.game.Game;
import com.visitor.protocol.Types.*;
import com.visitor.helpers.Hashmap;

/**
 * Abstract class for the Passive card type.
 * @author pseudo
 */
public abstract class Passive extends Card {
    
    /**
     *
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Passive(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(name, cost, knowledge, text, owner);
    }
    
    @Override
    public void resolve(Game game) {
        game.putTo(controller, this, "play");
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return game.hasEnergy(controller, cost)
               && game.hasKnowledge(controller, knowledge)
               && game.canPlaySlow(controller);
    }  
}
