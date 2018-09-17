/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.actions;

import game.Game;
import enums.Knowledge;
import cards.Action;
import cards.Card;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author pseudo
 */

//This is a replication of card Serum visions from MtG
public class DrawAndOrder extends Action{
    
    public DrawAndOrder (String owner){
        super("Draw And Order", 1, getKnowledge(), 
                "Draw 1. Order 3", "action.png", owner);
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.BLUE, 1);
       return knowledge;
    }
    
    public void resolve(Game game) {
        game.draw(owner, 1);
        ArrayList<Card> cards = game.orderCards(owner, game.players.get(owner).deck.draw(3));
        game.players.get(owner).deck.insertAll(cards, 0);
    }
}
