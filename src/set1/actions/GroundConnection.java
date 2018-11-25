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
import java.util.HashMap;


/**
 *
 * @author pseudo
 */


public class GroundConnection extends Action{
    
    public GroundConnection (String owner){
        super("Ground Connection", 4, getKnowledge(), 
                "Remove all charge counters from all items", "action.png", owner);
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.BLACK, 2);
       return knowledge;
    }
    
    @Override
    public void resolve(Game game) {
        game.players.values().forEach(player -> player.items.forEach(item -> item.counters.remove(Counter.CHARGE)));
    }
}
