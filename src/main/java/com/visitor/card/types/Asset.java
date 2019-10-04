
package com.visitor.card.types;

import com.visitor.card.properties.Activatable;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;

/**
 * Abstract class for the Asset card type.
 * @author pseudo
 */
public abstract class Asset extends Card implements Activatable {
    
    
    //TODO: Eventually remove this.
    public Asset(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(name, cost, knowledge, text, owner);
        health = 3;
    }
    
    public Asset(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, int health, String owner) {
        super(name, cost, knowledge, text, owner);
        this.health = health;
    }
    
    @Override
    protected void duringResolve(Game game) {
        depleted = true;
        game.putTo(controller, this, PLAY);
    }
    
    protected boolean canActivateAdditional(Game game) {return true;}
    @Override
    public final boolean canActivate(Game game) {
        return !depleted && canActivateAdditional(game);
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
                .setType("Asset");
    }
}
