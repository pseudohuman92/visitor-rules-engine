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
import cards.Item;
import client.Client;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import set1.items.activations.TransferCableActivation;

/**
 *
 * @author pseudo
 */
public class TransferCable extends Item{
    
    public TransferCable (String owner){
        super("Transfer Cable", 1, getKnowledge(), 
                "Exhaust, Exhaust an item you control: Charge target item.", 
                "item.png", owner);
        values = new int[0];
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.WHITE, 1);
       return knowledge;
    }
    
    @Override
    public boolean canActivate(ClientGame game) {
        for (Item i : game.player.items){
            if (!i.uuid.equals(uuid) && !i.depleted)
                return !depleted;
        }
        return false;
    }
    
    @Override
    public void activate(Client client) {
        client.gameArea.addFilteredPlayerTargetListeners(this::activateHelper, card -> !card.uuid.equals(uuid), 1);
    }
    
    public void activateHelper(Client client, ArrayList<Serializable> selection){
        supplimentaryData.addAll(selection);
        client.gameArea.addTargetListeners(this::activateHelper2, 1);
    }
    
    public void activateHelper2(Client client, ArrayList<Serializable> selection){
        supplimentaryData.addAll(selection);
        super.activate(client, supplimentaryData);
        supplimentaryData = new ArrayList<>();
    }
    
    @Override
    public void activate(Game game) {
        depleted = true;
        game.deplete((UUID) supplimentaryData.get(0));
        super.activate(game);
    }
    
    @Override
    public Activation getActivation() {
        return new TransferCableActivation (this);
    }
}
