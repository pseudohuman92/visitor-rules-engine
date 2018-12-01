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
import helpers.Hashmap;
import java.util.UUID;


/**
 *
 * @author pseudo
 */

//This is a replication of card Serum visions from MtG
public class QuickCharge extends Action{
    
    public QuickCharge (String owner){
        super("Quick Charge", 1, new Hashmap(Knowledge.BLUE, 1), 
                "Charge target item. Draw a card.", "action.png", owner);
        values = new int[0];
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
        supplimentaryData.forEach(uuid -> game.charge((UUID)uuid));
        game.draw(owner, 1);
    }
}
