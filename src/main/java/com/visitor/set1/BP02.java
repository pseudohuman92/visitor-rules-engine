/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Activation;
import com.visitor.card.types.Passive;
import com.visitor.game.Event;
import static com.visitor.game.Event.DISCARD;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.BLACK;

/**
 *
 * @author pseudo
 */
public class BP02 extends Passive implements Triggering {

    public BP02(String owner) {
        super("BP02", 1, new Hashmap(BLACK, 2), 
                "Trigger - When opponent discards a card\n" +
                "  Deal 2 damage to opponent.", owner);
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
        if (event.label.equals(DISCARD) 
                && ((String)event.eventData.get(0)).equals(game.getOpponentName(controller))){
            
            String discardingPlayer = ((String)event.eventData.get(0));
            game.addToStack(new Activation(controller,
            "Deal 2 damage to opponent",
            (x) -> {
                game.dealDamage(id, game.getUserId(discardingPlayer), 2);
            }, new Arraylist<>(id).putIn(game.getUserId(discardingPlayer))));
        }
    }
}
