/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package set1.black;

import card.Card;
import card.properties.Targeting;
import card.types.Item;
import game.ClientGame;
import game.Game;
import enums.Knowledge;
import client.Client;
import helpers.Hashmap;
import java.util.UUID;
import network.Message;

/**
 *
 * @author pseudo
 */
public class GarbageCannon extends Item implements Targeting {
    
    public GarbageCannon (String owner){
        super("Garbage Cannon", 4, new Hashmap(Knowledge.BLACK, 3), 
                "Sacrifice an item, Activate: Opponent purges 5. If sacrificed item belongs to him, he purges 10 instead.", 
                "item.png", owner);
    }

    @Override
    public boolean canActivate(ClientGame game) {
        return (!game.player.items.isEmpty())&&(!depleted);
    }
    
    @Override
    public void activate(Client client) {
        client.gameArea.getPlayAreaTargets(this::validTarget, 1, (client1, targets) -> {
        client1.gameConnection.send(Message.activate(client1.game.uuid, client.username, uuid, targets)); });
    }
    
    @Override
    public void activate(Game game) {
        depleted = true;
        Card c = game.getItemByID((UUID)supplementaryData.get(0));
        game.destroy(c.uuid);
        if (c.owner.equals(game.getOpponentName(controller))) {
            game.purge(game.getOpponentName(controller), 10);
        } else {
            game.purge(game.getOpponentName(controller), 5);
        }
    }

    @Override
    public boolean validTarget(Card c) {
        return c.controller.equals(controller);
    }
}
