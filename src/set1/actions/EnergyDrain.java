/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.actions;

import game.Game;
import enums.Knowledge;
import cards.Action;
import client.Client;
import game.ClientGame;
import java.util.HashMap;
import java.util.UUID;


/**
 *
 * @author pseudo
 */

//This is a replication of card Serum visions from MtG
public class EnergyDrain extends Action{
    
    public EnergyDrain (String owner){
        super("Energy Drain", 1, getKnowledge(), 
                "Deplete target item. Draw a card.", "action.png", owner);
        values = new int[0];
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.RED, 1);
       return knowledge;
    }
    
    public boolean canPlay(ClientGame game){ 
        return super.canPlay(game) 
               && (!game.player.items.isEmpty() 
                   || !game.opponent.items.isEmpty());
    }
    
    public void play(Client client){
        client.gameArea.addTargetListeners(super::play, 1);
    }
    
    public void resolve(Game game) {
        supplimentaryData.forEach(uuid -> game.deplete((UUID)uuid));
        game.draw(owner, 1);
    }
}
