/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inactive;

import card.Card;
import card.properties.Targeting;
import card.properties.XValued;
import card.types.Action;
import card.types.Ally;
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
public class Bribery extends Action implements Targeting, XValued {
    
    /**
     *
     * @param owner
     */
    public Bribery(String owner) {
        super("Bribery", 2, new Hashmap(BLACK, 1), "Posses target ally that costs X or less.", "action.png", owner);
    }
    
    @Override
    public boolean canPlay(ClientGame game){ 
        return super.canPlay(game) && game.hasValidTargetsInPlay(this::validTarget, 1);
    }
    
    @Override
    public void play(Client client) {
        client.gameArea.getXValue(this::isXValid, (client1, x) -> {
        supplementaryData.add(0, x);
        client1.gameArea.getPlayAreaTargets(this::validTarget, 1, (client2, targets) -> {
        supplementaryData.add(1, targets);
        client2.gameConnection.send(Message.play(client2.game.id, client2.username, id, supplementaryData)); }); });
    }
    
    /**
     * Called by server when this card is played.
     * Default behavior is that it deducts the energy cost of the card, 
     * removes it from player's hand and then resolves the card.
     * @param game
     */
    @Override
    public void play(Game game) {
        Player player = game.players.get(owner);
        player.hand.remove(this);
        player.energy -= cost + (int)supplementaryData.get(0);
        resolve(game);
    }
    
    @Override
    public void resolve (Game game){
        game.possess((UUID)supplementaryData.get(1), controller);
        game.players.get(owner).discardPile.add(this);
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Ally && c.cost <= (int)supplementaryData.get(0));
    }

    @Override
    public boolean isXValid(Client client, int x) {
        return (x + 2 <= client.game.player.energy);
    }
    
    
}
