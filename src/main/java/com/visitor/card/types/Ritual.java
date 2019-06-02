
package com.visitor.card.types;

import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;


/**
 * Abstract class for the Spell card type.
 * @author pseudo
 */
public abstract class Ritual extends Card {
    
    public Ritual(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(name, cost, knowledge, text, owner);
    }
    
    @Override
    protected abstract void duringResolve(Game game);
    
    @Override
    protected void afterResolve(Game game) {
        game.putTo(controller, this, SCRAPYARD);
    }    

    @Override
    public boolean canPlay(Game game){ 
        return game.hasEnergy(controller, cost)
               && game.hasKnowledge(controller, knowledge)
               && game.canPlaySlow(controller);
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Ritual");
    }
}
