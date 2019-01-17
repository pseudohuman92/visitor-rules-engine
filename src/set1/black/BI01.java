/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package set1.black;

import card.types.Item;
import client.Client;
import enums.Knowledge;
import game.ClientGame;
import game.Game;
import helpers.Hashmap;
import network.Message;

/**
 *
 * @author pseudo
 */
public class BI01 extends Item {
    
    public BI01 (String owner){
        super("BI01", 1, new Hashmap(Knowledge.BLACK, 1), 
                "1, Activate: Loot 1", 
                "item.png", owner);
    }

    @Override
    public boolean canActivate(ClientGame game) {
        return (game.player.energy > 0)&&(!depleted);
    }
    
    @Override
    public void activate(Client client) {
        client.gameConnection.send(Message.activate(client.game.id, client.username, id, null));
    }
    
    @Override
    public void activate(Game game) {
        game.removeEnergy(controller, 1);
        game.deplete(id);
        game.loot(controller, 1);
    }
}
