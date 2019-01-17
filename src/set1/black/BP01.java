/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package set1.black;

import card.properties.Triggering;
import card.types.Passive;
import static enums.EventLabel.*;
import enums.Knowledge;
import static enums.Subtype.Trap;
import game.Event;
import game.Game;
import helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public class BP01 extends Passive implements Triggering {
    
    public BP01 (String owner){
        super("Booby Trap", 1, new Hashmap(Knowledge.BLACK, 2), 
                "Trigger - opponent draws a card during your turn: Destroy Booby Trap, then opponent Purge 6.", 
                "passive.png", owner);
        subtypes.add(Trap);
    }

    @Override
    public void checkEvent(Game game, Event event) {
        if (event.label == DRAW){
            if (game.turnPlayer.equals(controller) && 
                    game.getOpponentName(controller).equals(event.eventData.get(0))){
                game.destroy(id);
                game.purge(game.getOpponentName(controller), 6);
            }
        }
    }
}
