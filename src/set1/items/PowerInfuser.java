/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items;

import cards.Activation;
import game.ClientGame;
import game.Game;
import enums.Knowledge;
import cards.Card;
import cards.Item;
import client.Client;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;
import set1.items.activations.PowerInfuserActivation;

/**
 *
 * @author pseudo
 */
public class PowerInfuser extends Item{
    
    public PowerInfuser (String owner){
        super("Power Infuser", 1, getKnowledge(), 
                "1, Exhaust: Charge target item.", 
                "item.png", owner);
        values = new int[0];
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.BLUE, 1);
       return knowledge;
    }
    
    @Override
    public boolean canActivate(ClientGame game) {
        return game.player.energy > 0 && !depleted;
    }
    
    @Override
    public void activate(Client client) {
        client.gameArea.addTargetListeners(super::activate, 1);
    }

    @Override
    public void activate(Game game) {
        game.players.get(owner).energy--;
        depleted = true;
        super.activate(game);
    }
    
    @Override
    public Activation getActivation() {
        return new PowerInfuserActivation (this);
    }

}
