/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items;

import cards.Activation;
import cards.Card;
import cards.Item;
import enums.Knowledge;
import game.ClientGame;
import game.Game;
import game.Player;
import java.util.HashMap;
import set1.items.activations.EmpBombActivation;
import set1.items.activations.MysticalTeachingsGainLifeActivation;
import set1.items.activations.MysticalTeachingsStartTrigger;

/**
 *
 * @author pseudo
 */
public class MysticalTeachings extends Item {
    public MysticalTeachings (String owner){
        super("Mystical Teachings", 3, getKnowledge(), 
                "When Mystical Teachings enters play, you gain 5 sanity.<br><br>" +
"At the start of your turn, if you have more than 30 sanity, add a transcendence counter on Mystical Teachings. "
                        + "If Mystical Teachings has 5 or more transcendence counters on it, you win the game.", 
                "item.png", owner);
        values = new int[0];
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.WHITE, 2);
       return knowledge;
    }
    
    @Override
    public void resolve(Game game) {
        super.resolve(game);
        game.addToStack(new MysticalTeachingsGainLifeActivation(this));
    }
    
    public Activation getPlayerStartTrigger() {
        return new MysticalTeachingsStartTrigger(this);
    }
}
