/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import cards.Card;
import cards.Item;
import helpers.Hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import network.Connection;
import network.Message;

/**
 *
 * @author pseudo
 */
public class Game {
    
    //(player_name, player) pairs
    public Hashmap<String, Player> players;
    public Hashmap<String, Connection> communicationConnections;
    public Hashmap<String, Connection> interactionConnections;
    public ArrayList<Card> stack;
    public String turnPlayer;
    public String activePlayer;
    public Phase phase;
    public int turnCount;
    public int passCount;
    public boolean startTriggered;
    public UUID uuid;
    
    public Game (Table table) {
        uuid = UUID.randomUUID();
        players = new Hashmap<>();
        stack = new ArrayList<>();
        communicationConnections = new Hashmap<>();
        interactionConnections = new Hashmap<>();
        
        table.creatorDeck.shuffle();
        table.opponentDeck.shuffle();
        
        players.put(table.creator, new Player(table.creator, table.creatorDeck));
        players.put(table.opponent, new Player(table.opponent, table.opponentDeck));
        
        phase = Phase.MULLIGAN;
        turnPlayer = (Math.random() < 0.5)?table.creator:table.opponent;
        activePlayer = turnPlayer;
        turnCount = 0;
        passCount = 0;
        players.get(table.creator).draw(5);
        players.get(table.opponent).draw(5);
    }
    
    public void changePhase(boolean jump){
        switch(phase) {
            case MULLIGAN:
                newTurn();
                break;
            case BEGIN:
                phase = jump?Phase.MAIN:Phase.BEGIN_RESOLVING;
                break;
            case BEGIN_RESOLVING:
                passCount = 0;
                startTriggered = true;
                activePlayer = turnPlayer;
                phase = Phase.BEGIN;
                break;
            case MAIN:
                phase = jump?Phase.END:Phase.MAIN_RESOLVING;
                break;
            case MAIN_RESOLVING:
                passCount = 0;
                activePlayer = turnPlayer;
                phase = Phase.MAIN;
                break;
            case END:
                if (jump) {
                    newTurn();
                } else {
                    phase = Phase.END_RESOLVING;
                }
                break;
            case END_RESOLVING:
                passCount = 0;
                activePlayer = turnPlayer;
                phase = Phase.END;
                break;
        }
    }
    
    void newTurn(){
        phase = Phase.BEGIN;
        if(turnCount > 0){
            turnPlayer = getOpponentName(turnPlayer);
            players.get(turnPlayer).draw(1);
        }
        startTriggered = false;
        activePlayer = turnPlayer;
        passCount = 0;
        players.get(turnPlayer).draw(1);
        players.get(turnPlayer).newTurn();
        turnCount++;
    }
    
    public String getOpponentName(String playerName){
        for(String name : players.keySet()){
            if(!name.equals(playerName)){
                return name;
            }
        }
        return null;
    }
    
    public ClientGame toClientGame(String playerName) {
        Player opponent = players.get(getOpponentName(playerName));
        return new ClientGame (players.get(playerName), opponent.toOpponent(), 
                stack, turnPlayer, activePlayer, phase, uuid);
    }
    
    public void discard(String username, int count){
        Connection connection = interactionConnections.get(username);
        connection.send(Message.discard(toClientGame(username), count));
        Message message = connection.receive();
        if (message != null) {
            Player player = players.get(username);
            player.discard((ArrayList<UUID>)message.object);
        }
    }
    
    public void draw(String username, int count){
        Player player = players.get(username);
        player.draw(count);
    }
    
    public void destroy(UUID uuid){
        Item item = getItem(uuid);
        players.get(item.owner).items.remove(item);
        players.get(item.owner).discardPile.add(item);
    }
    
    public void charge(UUID uuid){
        for (Player player : players.values()) {
            for (Card card : player.items) {
                if (card.uuid.equals(uuid)){
                    card.depleted = false;
                    return;
                }
            }
        }
    }
    
    public void deplete(UUID uuid){
        for (Player player : players.values()) {
            for (Card card : player.items) {
                if (card.uuid.equals(uuid)){
                    card.depleted = true;
                    break;
                }
            }
        }
    }
    
    public Player getPlayer(UUID playerID){
        for (Player player : players.values()) {
            if(player.uuid.equals(playerID)){ 
                return player;
            }
        }
        return null;
    }
    
    public Item getItem(UUID itemID){
        for (Player player : players.values()) {
            for (Item item : player.items){
                if(item.uuid.equals(itemID)){ 
                    return item;
                }
            }
        }
        return null;
    }
    
    public void switchOwner(UUID itemID){
        Item item = getItem(itemID);
        players.get(item.owner).items.remove(item);
        players.get(getOpponentName(item.owner)).items.add(item);
        item.owner = getOpponentName(item.owner);
    }

    public void win(String player) {
        Connection connection = interactionConnections.get(player);
        connection.send(Message.win());
        connection.closeConnection();
        connection = interactionConnections.get(getOpponentName(player));
        connection.send(Message.lose());
        connection.closeConnection();
    }

    public ArrayList<Card> orderCards(String username, ArrayList<Card> cards) {
        Connection connection = interactionConnections.get(username);
        connection.send(Message.order(cards));
        Message message = connection.receive();
        if (message != null) {
            cards = Card.sortByUUID(cards, (ArrayList<UUID>)message.object);
            return cards;
        }
        return null;
    }
    
    public ArrayList<Card> getPlayerStartTriggers() {
        ArrayList<Card> triggers = new ArrayList<>();
        players.get(turnPlayer).items.forEach((card) -> {
            Card c = card.getPlayerStartTrigger();
            if (c != null)
                triggers.add(c);
        });
        return triggers;
    }
    
    public void addToStack(Card c){
        stack.add(c);
        passCount = 0;
        switch (phase) {
            case BEGIN_RESOLVING:
                phase = Phase.BEGIN;
                break;
            case MAIN_RESOLVING:
                phase = Phase.MAIN;
                break;
            case END_RESOLVING:
                phase = Phase.END;
                break;
        }
    }
    
    public void addToStack(ArrayList<Card> c){
        stack.addAll(c);
        passCount = 0;
        switch (phase) {
            case BEGIN_RESOLVING:
                phase = Phase.BEGIN;
                break;
            case MAIN_RESOLVING:
                phase = Phase.MAIN;
                break;
            case END_RESOLVING:
                phase = Phase.END;
                break;
        }
    }
}
