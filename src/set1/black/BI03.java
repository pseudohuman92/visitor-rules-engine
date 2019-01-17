/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package set1.black;

import card.Card;
import card.properties.Targeting;
import card.types.Item;
import client.Client;
import enums.Knowledge;
import game.ClientGame;
import game.Game;
import helpers.Hashmap;
import java.util.UUID;
import network.Message;

/**
 *
 * @author pseudo
 */
public class BI03 extends Item implements Targeting {
    
    public BI03 (String owner){
        super("BI03", 4, new Hashmap(Knowledge.BLACK, 2), 
                "Sacrifice an Item: Gain 1 Energy. If that item is owned by the opponent gain 1 additional energy.", 
                "item.png", owner);
    }

    @Override
    public boolean canActivate(ClientGame game) {
        return game.hasAnItem(controller);
    }
    
    @Override
    public void activate(Client client) {
        client.gameArea.getPlayAreaTargets(this::validTarget, 1, (client1, targets) -> {
        client1.gameConnection.send(Message.activate(client1.game.id, client.username, id, targets)); });
    }
    
    @Override
    public void activate(Game game) {
        UUID sacID = (UUID)supplementaryData.get(0);
        game.destroy(sacID);
        if (game.ownedByOpponent(sacID)) {
            game.addEnergy(controller, 2);
        } else {
            game.addEnergy(controller, 1);
        }
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item && c.controller.equals(controller));
    }
}
