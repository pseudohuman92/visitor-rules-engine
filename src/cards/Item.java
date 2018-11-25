/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import client.Client;
import enums.Knowledge;
import enums.Type;
import game.ClientGame;
import game.Game;
import game.Phase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import network.Message;

/**
 *
 * @author pseudo
 */
public abstract class Item extends Card {
    
    public Item(String name, int cost, HashMap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, image, Type.ITEM, owner);
    }
    
    public Item(Item c){
        super (c);
    }
    
    public Item(Item c, String text){
        super (c, text);
    }
    
    @Override
    public void resolve(Game game) {
        depleted = true;
        game.players.get(owner).items.add(this);
    }
    
    @Override
    public boolean canPlay(ClientGame game){ 
        return (game.player.energy >= cost)
               && game.player.hasKnowledge(knowledge)
               && game.turnPlayer.equals(owner)
               && game.stack.isEmpty()
               && game.phase == Phase.MAIN;
    }
    
    // Called by the client to see if this item can be activated
    public boolean canActivate(ClientGame game){ return false; }

    // Called by the client when you choose to activate this item
    public void activate(Client client){
        client.gameConnection.send(Message.activate(client.game.uuid, client.username, uuid, new ArrayList<>()));
    }
    
    public void activate(Client client, ArrayList<Serializable> targets){
        client.gameConnection.send(Message.activate(client.game.uuid, client.username, uuid, targets));
    }
    
    // Called by the server when you activated this item
    public void activate(Game game){
        game.addToStack(getActivation());
        game.activePlayer = game.getOpponentName(game.activePlayer);
    }
    
    //Called at the start of your turn
    public Activation getPlayerStartTrigger(){ return null; }
    
    public Activation getActivation(){ return null; }
}
