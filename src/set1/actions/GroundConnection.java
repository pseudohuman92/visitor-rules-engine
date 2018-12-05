/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.actions;

import game.Game;
import enums.Knowledge;
import cards.Action;
import enums.Counter;
import helpers.Hashmap;


/**
 *
 * @author pseudo
 */


public class GroundConnection extends Action{
    
    public GroundConnection (String owner){
        super("Ground Connection", 4, new Hashmap(Knowledge.BLACK, 2), 
                "Remove all charge counters from all items", "action.png", owner);
    }
    
    @Override
    public void resolve(Game game) {
        game.players.values().forEach(player -> player.items.forEach(item -> item.counters.remove(Counter.CHARGE)));
        super.resolve(game);
    }
}
