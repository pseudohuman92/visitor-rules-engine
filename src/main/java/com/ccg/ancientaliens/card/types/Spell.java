
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.Knowledge;


/**
 * Abstract class for the Spell card type.
 * @author pseudo
 */
public abstract class Spell extends Card {
    
    /**
     *
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Spell(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(name, cost, knowledge, text, owner);
    }

    @Override
    public boolean canPlay(Game game){ 
        return game.hasEnergy(controller, cost)
               && game.hasKnowledge(controller, knowledge)
               && game.isActive(controller);
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Spell");
    }
}
