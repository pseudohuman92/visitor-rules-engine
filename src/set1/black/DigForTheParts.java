/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.black;

import card.types.Action;
import card.Card;
import card.properties.Targeting;
import card.types.Item;
import client.Client;
import static enums.Knowledge.BLACK;
import game.ClientGame;
import game.Game;
import game.Player;
import helpers.Hashmap;
import java.util.UUID;
import network.Message;

/**
 *
 * @author pseudo
 */
public class DigForTheParts extends Action implements Targeting {
    
    /**
     *
     * @param owner
     */
    public DigForTheParts(String owner) {
        super("Dig For The Parts", 1, new Hashmap(BLACK, 1), "Additional Cost - Sacrifice an item.<br>Draw 2 cards.", "action.png", owner);
    }
    
    @Override
    public boolean canPlay(ClientGame game){ 
        return super.canPlay(game) && game.hasValidTargetsInPlay(this::validTarget, 1);
    }
    
    @Override
    public void play(Client client) {
        client.gameArea.getPlayAreaTargets(this::validTarget, 1, (client2, targets) -> {
        supplementaryData.add(0, targets.get(0));
        client2.gameConnection.send(Message.play(client2.game.uuid, client2.username, uuid, supplementaryData)); });
    }
    
    @Override
    public void play(Game game) {
        Player player = game.players.get(owner);
        player.hand.remove(this);
        player.energy -= cost;
        game.destroy((UUID)supplementaryData.get(0));
        resolve(game);
    }
    
    @Override
    public void resolve (Game game){
        game.draw(controller, 2);
        game.players.get(owner).discardPile.add(this);
    }

    @Override
    public boolean validTarget(Card c) {
        return (c.controller.equals(controller) && c instanceof Item);
    }    
    
}
