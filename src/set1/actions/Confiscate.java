/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.actions;

import game.Game;
import enums.Knowledge;
import cards.Action;
import cards.Item;
import client.Client;
import game.ClientGame;
import helpers.Hashmap;
import java.util.HashMap;
import java.util.UUID;


/**
 *
 * @author pseudo
 */

//This is a replication of card Serum visions from MtG
public class Confiscate extends Action{
    
    public Confiscate (String owner){
        super("Confiscate", 3, new Hashmap<>(Knowledge.RED, 2), 
                "Shuffle target item to your deck.", "action.png", owner);
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
        supplimentaryData.forEach(uuid -> {
            Item item = game.getItem((UUID)uuid);
            game.players.get(item.owner).items.remove(item);
            game.players.get(owner).deck.insertTo(item, 0);
            game.players.get(owner).deck.shuffle();
        });
        super.resolve(game);
    }
}
