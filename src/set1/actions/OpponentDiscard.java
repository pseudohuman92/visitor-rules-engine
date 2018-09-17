/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.actions;

import game.Game;
import enums.Knowledge;
import cards.Card;
import enums.Type;
import cards.Action;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author pseudo
 */
public class OpponentDiscard extends Action{
    
    public OpponentDiscard (String owner){
        super("Opponent Discard", 0, getKnowledge(), 
                "Target opponent discards %s cards.", "action.png", owner);
        values = new int[2];
        values[0] = 2;
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.GREEN, 1);
       return knowledge;
    }
    
    public void resolve(Game game) {
        game.discard(game.getOpponentName(owner), values[0]);
    }
}
