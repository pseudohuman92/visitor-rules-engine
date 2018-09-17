/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import enums.Knowledge;
import enums.Type;
import game.ClientGame;
import java.util.HashMap;

/**
 *
 * @author pseudo
 */
public abstract class Action extends Card {
    
    public Action(String name, int cost, HashMap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, image, Type.ACTION, owner);
    }
    
    public Action(Action c){
        super (c);
    }
    
    public Action(Action c, String text){
        super (c, text);
    }
    
    public boolean canPlay(ClientGame game){ 
        return (game.player.energy >= cost)
               && game.player.hasKnowledge(knowledge);
    }

}
