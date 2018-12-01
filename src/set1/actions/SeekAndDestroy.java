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
import helpers.Hashmap;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class SeekAndDestroy extends Action{
    
    public SeekAndDestroy (String owner){
        super("Seek And Destroy", 1, new Hashmap(Knowledge.RED, 1).put(Knowledge.GREEN, 2), 
                "Destroy %s items.", "action.png", owner);
        values = new int[1];
        values[0] = 1;
    }

    public void play(Client client){
        client.gameArea.addTargetListeners(super::play, 1);
    }
    
    @Override
    public void resolve(Game game) {
        for(Serializable uuid : supplimentaryData){
            game.destroy((UUID)uuid);
        }
    }
}
