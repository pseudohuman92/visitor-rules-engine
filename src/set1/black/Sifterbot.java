/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package set1.black;

import card.types.Item;
import game.ClientGame;
import game.Game;
import enums.Knowledge;
import client.Client;
import game.Player;
import helpers.Hashmap;
import network.Message;

/**
 *
 * @author pseudo
 */
public class Sifterbot extends Item {
    
    public Sifterbot (String owner){
        super("Sifterbot", 1, new Hashmap(Knowledge.BLACK, 1), 
                "1, Activate: Loot 1", 
                "item.png", owner);
    }

    @Override
    public boolean canActivate(ClientGame game) {
        return (game.player.energy > 0)&&(!depleted);
    }
    
    @Override
    public void activate(Client client) {
        client.gameConnection.send(Message.activate(client.game.uuid, client.username, uuid, null));
    }
    
    @Override
    public void activate(Game game) {
        Player p = game.getPlayerByName(controller);
        p.energy--;
        depleted = true;
        game.loot(controller, 1);
    }
}
