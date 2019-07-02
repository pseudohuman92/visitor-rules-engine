
package com.visitor.card.types;

import com.visitor.card.properties.Activatable;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;
import java.util.UUID;

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
    
    @Override
    public void dealDamage(Game game, int damageAmount, UUID source) {
        if (health <= damageAmount){
            health = 0;
            destroy(game);
        } else {
            health -= damageAmount;
        }
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted;
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
