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
import static enums.Subtype.*;
import game.ClientGame;
import game.Game;
import helpers.Hashmap;
import java.util.UUID;
import network.Message;

/**
 *
 * @author pseudo
 */
public class BI02 extends Item implements Targeting {
    
    public BI02 (String owner){
        super("Garbage Cannon", 4, new Hashmap(Knowledge.BLACK, 3), 
                "Sacrifice an item, Activate: Opponent purges 5. If sacrificed item belongs to him, he purges 10 instead.", 
                "item.png", owner);
        subtypes.add(Weapon);
    }

    @Override
    public boolean canActivate(ClientGame game) {
        return (!game.player.inPlayCards.isEmpty())&&(!depleted);
    }
    
    @Override
    public void activate(Client client) {
        client.gameArea.getPlayAreaTargets(this::validTarget, 1, (client1, targets) -> {
        client1.gameConnection.send(Message.activate(client1.game.id, client.username, id, targets)); });
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        String oppName = game.getOpponentName(controller);
        UUID targetID = (UUID)supplementaryData.get(0);
        game.destroy(targetID);
        if (game.ownedByOpponent(targetID)) {
            game.purge(oppName, 10);
        } else {
            game.purge(oppName, 5);
        }
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Card && c.controller.equals(controller));
    }
}
