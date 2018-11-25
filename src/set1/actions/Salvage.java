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

public class Salvage extends Action{
    
    public Salvage (String owner){
        super("Salvage", 1, getKnowledge(), 
                "As an additional cost to play Salvage, destroy an item you control.\n" +
                "Draw 3 cards.", "action.png", owner);
        values = new int[0];
    }
    
    static private HashMap<Knowledge,Integer> getKnowledge(){
       HashMap<Knowledge,Integer> knowledge = new HashMap<>();
       knowledge.put(Knowledge.RED, 1);
       return knowledge;
    }
    
    @Override
    public boolean canPlay(ClientGame game){ 
        return super.canPlay(game) && !game.player.items.isEmpty();
    }
    
    @Override
    public void play(Client client){
        client.gameArea.addPlayerTargetListeners(super::play, 1);
    }
    
    @Override
    public void play(Game game){
        game.destroy((UUID)supplimentaryData.get(0));
        super.play(game);
    }
    
    @Override
    public void resolve(Game game) {
        game.draw(owner, 3);
    }
}
