
package com.visitor.card.types;

import com.visitor.card.properties.Activatable;
import com.visitor.card.properties.Damageable;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.protocol.Types.Knowledge;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import java.util.UUID;

/**
 * Abstract class for the Item card type.
 * @author pseudo
 */
public abstract class Item extends Card implements Activatable, Damageable {
    
    int health;
    
    //TODO: Eventually remove this.
    public Item(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(name, cost, knowledge, text, owner);
        health = 3;
    }
    
    public Item(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, int health, String owner) {
        super(name, cost, knowledge, text, owner);
        this.health = health;
    }
    
    
    @Override
    public void resolve(Game game) {
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
    public void copyPropertiesFrom(Card c){
        super.copyPropertiesFrom(c);
        if (c instanceof Item){
            health = ((Item) c).health;
        }
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Item")
                .setHealth(health);
    }
}
