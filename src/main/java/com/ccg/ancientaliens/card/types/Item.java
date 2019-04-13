
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.card.properties.Activatable;
import com.ccg.ancientaliens.card.properties.Damageable;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.protocol.Types.Knowledge;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;

/**
 * Abstract class for the Item card type.
 * @author pseudo
 */
public abstract class Item extends Card implements Activatable, Damageable {
    
    int health;
    
    public Item(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(name, cost, knowledge, text, owner);
        health = 5;
    }
    
    
    @Override
    public void resolve(Game game) {
        depleted = true;
        game.putTo(controller, this, "play");
    }
    
    @Override
    public int dealDamage(Game game, int damageAmount) {
        int tmp = Math.min(health, damageAmount);
        if (health <= damageAmount){
            health = 0;
            game.extractCard(id);
            game.putTo(controller, this, "scrapyard");
        } else {
            health -= damageAmount;
        }
        return tmp;
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
                .setType("Item")
                .setHealth(health);
    }
}
