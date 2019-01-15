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
public class MatterCondenser extends Item implements Targeting {
    
    public MatterCondenser (String owner){
        super("Matter Condenser", 4, new Hashmap(Knowledge.BLACK, 2), 
                "Sacrifice an Item: Gain 1 Energy. If that item is owned by the opponent gain 1 additional energy.", 
                "item.png", owner);
    }

    @Override
    public boolean canActivate(ClientGame game) {
        return !game.player.items.isEmpty();
    }
    
    @Override
    public void activate(Client client) {
        client.gameArea.getPlayAreaTargets(this::validTarget, 1, (client1, targets) -> {
        client1.gameConnection.send(Message.activate(client1.game.uuid, client.username, uuid, targets)); });
    }
    
    @Override
    public void activate(Game game) {
        Card c = game.getItemByID((UUID)supplementaryData.get(0));
        game.destroy(c.uuid);
        if (c.owner.equals(game.getOpponentName(controller))) {
            game.players.get(controller).energy +=2;
        } else {
            game.players.get(controller).energy +=1;
        }
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item && c.controller.equals(controller));
    }
}
