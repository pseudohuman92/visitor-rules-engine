/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package set1.black;

import card.properties.Triggering;
import card.types.Passive;
import game.Event;
import static enums.EventLabel.POSSESS;
import enums.Knowledge;
import game.Game;
import helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public class AccumulatedLoss extends Passive implements Triggering {
    
    public AccumulatedLoss (String owner){
        super("Accumulated Loss", 2, new Hashmap(Knowledge.BLACK, 2), 
                "Trigger - When you possess a card: controller of the possesed card Purges 2", 
                "passive.png", owner);
    }

    @Override
    public void checkEvent(Game game, Event event) {
        if (event.label == POSSESS){
            if (controller.equals(event.eventData.get(1))){
                game.purge((String) event.eventData.get(0), 2);
            }
        }
    }
}
