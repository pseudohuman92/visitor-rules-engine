/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.black;

import card.properties.XValued;
import card.types.Action;
import client.Client;
import static enums.Knowledge.BLACK;
import game.Game;
import game.Player;
import helpers.Hashmap;
import network.Message;

/**
 *
 * @author pseudo
 */
public class TrashToTreasure extends Action implements XValued {
    
    /**
     *
     * @param owner
     */
    public TrashToTreasure(String owner) {
        super("Trash To Treasure", 0, new Hashmap(BLACK, 1), "Loot X.", "action.png", owner);
    }
    
    
    @Override
    public void play(Client client) {
        client.gameArea.getXValue(this::isXValid, (client1, x) -> {
        supplementaryData.add(0, x);
        client1.gameConnection.send(Message.play(client1.game.id, client1.username, id, supplementaryData)); });
    }

    @Override
    public void play(Game game) {
        Player player = game.players.get(owner);
        player.hand.remove(this);
        player.energy -= cost + (int)supplementaryData.get(0);
        resolve(game);
    }
    
    @Override
    public void resolve (Game game){
        game.loot(controller, (int)supplementaryData.get(0));
        game.discardAfterPlay(this);
    }

    @Override
    public boolean isXValid(Client client, int x) {
        return (x <= client.game.player.energy);
    }
}
