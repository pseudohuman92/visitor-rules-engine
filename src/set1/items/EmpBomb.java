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

/**
 *
 * @author pseudo
 */
public class EmpBomb extends Item {
    public EmpBomb (String owner){
        super("Emp Bomb", 4, getKnowledge(), 
                "4, Exhaust, Destroy: Destroy all items.", 
                "item.png", owner);
        values = new int[0];
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.BLACK, 2);
       return knowledge;
    }
    
    @Override
    public boolean canActivate(ClientGame game){ 
        return game.player.energy > 3 && !depleted; 
    }
    
    @Override
    public void activate(Game game){
        Player player = game.players.get(owner);
        player.energy -= 4;
        player.items.remove(this);
        player.discardPile.add(this);
        super.activate(game);
    }
    
    @Override
    public Activation getActivation(){ 
        return new EmpBombActivation(this); 
    }
}
