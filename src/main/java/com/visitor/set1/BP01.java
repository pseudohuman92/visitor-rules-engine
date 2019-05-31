/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Ability;
import com.visitor.card.types.Passive;
import com.visitor.game.Event;
import static com.visitor.game.Event.POSSESSION;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.BLACK;

/**
 *
 * @author pseudo
 */
public class BP01 extends Passive implements Triggering {

    public BP01(String owner) {
        super("BP01", 2, new Hashmap(BLACK, 1), 
                "Trigger - When you possess a card \n" +
                "  Deal 2 damage to possessed card's controller", owner);
    }

    @Override
    public void resolve(Game game) {
        super.resolve(game);
        game.registerTriggeringCard(controller, this);
    }
    
    @Override
    public void destroy(Game game) {
        super.destroy(game);
        game.removeTriggeringCard(this);
    }
    
    @Override
    public void checkEvent(Game game, Event event) {
        if (event.label.equals(POSSESSION) 
                && ((String)event.eventData.get(1)).equals(controller)){
            String oldOwner = ((String)event.eventData.get(0));
            
            game.addToStack(new Ability(this,
            "Deal 2 damage to possessed card's controller",
            (x) -> {
                game.dealDamage(id, game.getUserId(oldOwner), 2);
            }, new Arraylist<>(game.getUserId(oldOwner))));
        }
    }
}
