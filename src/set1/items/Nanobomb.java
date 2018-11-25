/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items;

import cards.Activation;
import cards.Item;
import client.Client;
import game.ClientGame;
import game.Game;
import java.util.HashMap;
import set1.items.activations.NanobombActivation;

/**
 *
 * @author pseudo
 */
public class Nanobomb extends Item {
    public Nanobomb (String owner){
        super("Nanobomb", 0, new HashMap<>(), 
                "Destroy: Target player loses 1 life.", 
                "item.png", owner);
        values = new int[0];
    }
    
    @Override
    public boolean canActivate(ClientGame game){ 
        return true; 
    }
    
    @Override
    public void activate(Client client){
        client.gameArea.addPlayerSelector(super::activate);    
    }
    
    @Override
    public void activate(Game game){
        game.destroy(uuid);
        super.activate(game);
    }
    
    @Override
    public Activation getActivation(){ 
        return new NanobombActivation(this); 
    }
}
