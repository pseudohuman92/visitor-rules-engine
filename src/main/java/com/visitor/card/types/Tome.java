
package com.visitor.card.types;

import com.visitor.game.Game;
import com.visitor.protocol.Types.Knowledge;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

/**
 * Abstract class for the Tome card type.
 * @author pseudo
 */
public abstract class Tome extends Card {

    public Tome(String name, String text, String owner) {
        super(name, 0, new Hashmap<Knowledge, Integer>(), text, owner);
    }

    @Override
    public boolean canPlay(Game game){ return false; }
    
    @Override
    abstract public Hashmap<Knowledge, Integer> getKnowledgeType();
    
    @Override
    public void resolve(Game game){
        destroy(game);
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Tome")
                .setCost("");
    }
}