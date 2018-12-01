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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


/**
 *
 * @author pseudo
 */

public class Swindle extends Action{
    
    public Swindle (String owner){
        super("Swindle", 2, new Hashmap(Knowledge.RED, 2), 
                "Exchange an item you control with an item opponent controls that costs equal or less.", "action.png", owner);
    }

    @Override
    public boolean canPlay(ClientGame game){ 
        return super.canPlay(game) 
               && !game.player.items.isEmpty()
               && !game.opponent.items.isEmpty();
    }
    
    @Override
    public void play(Client client){
        client.gameArea.addPlayerTargetListeners(super::play, 1);
    }
    
    public void playHelper(Client client, ArrayList<Serializable> selection){
        supplimentaryData.addAll(selection);
        client.gameArea.addFilteredOpponentTargetListeners(this::playHelper2, card-> card.cost <= ((Item)selection.get(0)).cost, 1);
    }
    
    public void playHelper2(Client client, ArrayList<Serializable> selection){
        supplimentaryData.addAll(selection);
        super.play(client, supplimentaryData);
        supplimentaryData = new ArrayList<>();
    }
    
    @Override
    public void resolve(Game game) {
        game.switchOwner((UUID) supplimentaryData.get(0));
        game.switchOwner((UUID) supplimentaryData.get(1));
    }
}
