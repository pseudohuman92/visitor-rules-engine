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
public class PortableGateway extends Item implements Targeting {
    
    public PortableGateway (String owner){
        super("Portable Gateway", 3, new Hashmap(Knowledge.BLACK, 1), 
                "Activate, Destroy Portable Gateway: Draw a card from your void, then purge a card from your hand.", 
                "item.png", owner);
    }

    @Override
    public boolean canActivate(ClientGame game) {
        return !depleted && game.hasACardInVoid(controller);
    }
    
    @Override
    public void activate(Client client) {
        client.gameArea.getPlayerVoidTargets(this::validTarget, 1, (client1, targets) -> {
        client1.gameConnection.send(Message.activate(client1.game.id, client.username, id, null)); });
    }
    
    @Override
    public void activate(Game game) {
        UUID retID = (UUID)supplementaryData.get(0);
        game.destroy(id);
        game.drawFromVoid(controller, retID);
        game.purgeFromHand(controller, 1);
    }

    @Override
    public boolean validTarget(Card c) {
        return c.controller.equals(controller);
    }
}
