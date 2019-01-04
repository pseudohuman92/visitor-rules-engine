/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.black;

import card.types.Action;
import card.Card;
import card.types.Item;
import card.properties.Targeting;
import client.Client;
import static enums.Knowledge.BLACK;
import game.ClientGame;
import game.Game;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;
import network.Message;

/**
 *
 * @author pseudo
 */
public class Pickpocketing extends Action implements Targeting {
    
    /**
     *
     * @param owner
     */
    public Pickpocketing(String owner) {
        super("Pickpocketing", 3, new Hashmap(BLACK, 2), "Possess target item that costs 3 or less.", "action.png", owner);
    }
    
    @Override
    public boolean canPlay(ClientGame game){ 
        return super.canPlay(game) && game.hasValidTargetsInPlay(this::validTarget, 1);
    }
    
    @Override
    public void play(Client client) {
        client.gameConnection.send(Message.play(client.game.uuid, client.username, uuid, new ArrayList<>()));
    }
    
    @Override
    public void resolve (Game game){
        game.changeItemController((UUID)supplementaryData.get(0), controller);
        game.players.get(owner).discardPile.add(this);
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item && c.cost <= 3);
    }
    
    
}
