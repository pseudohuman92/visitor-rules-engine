
package com.visitor.card.types;

import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;


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
    
    public abstract void resolveEffect(Game game);
       
    public void resolve (Game game){ 
        resolveEffect(game);
        game.extractCard(id);
        game.putTo(controller, this, "scrapyard");
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
