
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.server.GameEndpoint;

import com.ccg.ancientaliens.protocol.ServerGameMessages.ServerGameMessage;
import com.ccg.ancientaliens.protocol.ServerGameMessages.UpdateGameState;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.*;
import static com.ccg.ancientaliens.protocol.Types.Phase.*;
import helpers.Hashmap;
import java.io.IOException;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;

/**
 *
 * @author pseudo
 */
public class Game {
    
    public Hashmap<String, Player> players;
    public Hashmap<String, GameEndpoint> connections;
    public String turnPlayer;
    public String activePlayer;
    public ArrayList<Card> stack;
    public Phase phase;
    public int turnCount;
    public int passCount;
    public UUID id;
    
    public Game (Table table) {
        id = randomUUID();
        players = new Hashmap<>();
        connections = new Hashmap<>();
        stack = new ArrayList<>();
        
        table.creatorDeck.shuffle();
        table.opponentDeck.shuffle();
        
        players.put(table.creator, new Player(table.creator, table.creatorDeck));
        players.put(table.opponent, new Player(table.opponent, table.opponentDeck));
        
        phase = MULLIGAN;
        turnPlayer = (random() < 0.5)?table.creator:table.opponent;
        activePlayer = turnPlayer;
        turnCount = 0;
        passCount = 0;
        players.get(table.creator).draw(5);
        players.get(table.opponent).draw(5);
    }
    
    public Game (String p1, String p2) {
        id = randomUUID();
        players = new Hashmap<>();
        connections = new Hashmap<>();
        stack = new ArrayList<>();
        
        players.put(p1, new Player(p1, new Deck(p1, true)));
        players.put(p2, new Player(p2, new Deck(p2, true)));
        
        phase = MULLIGAN;
        turnPlayer = (random() < 0.5)?p1:p2;
        activePlayer = turnPlayer;
        turnCount = 0;
        passCount = 0;
        players.get(p1).draw(5);
        players.get(p2).draw(5);
    }

    /*
    public void discard(String username, int count){
        Connection connection = connections.get(username);
        connection.send(Message.selectFromHand(toClientGame(username), count));
        Message message = connection.receive();
        if (message != null) {
            Player player = players.get(username);
            player.discard((ArrayList<UUID>)message.object);
        }
    }
 
    public void draw(String username, int count){
        Player player = players.get(username);
        player.draw(count);
        if (player.deck.deck.isEmpty()){
            lose(username);
        }
        processTrigger(Event.draw(username, count));
    }

    public void purge(String username, int count){
        Player player = players.get(username);
        player.purgeFromDeck(count);
        if (player.deck.deck.isEmpty()){
            lose(username);
        }
    }
    
    
    public void destroy(UUID id){
        Card item = getPlayAreaCardByID(id);
        players.get(item.owner).playArea.remove(item);
        players.get(item.owner).scrapyard.add(item);
    }
    

    public void ready(UUID id){
        for (Player player : players.values()) {
            for (Card card : player.playArea) {
                if (card.id.equals(id)){
                    card.depleted = false;
                    return;
                }
            }
        }
    }
    

    public void deplete(UUID id){
        for (Player player : players.values()) {
            for (Card card : player.playArea) {
                if (card.id.equals(id)){
                    card.depleted = true;
                    break;
                }
            }
        }
    }
    

    public Player getPlayerByID(UUID playerID){
        for (Player player : players.values()) {
            if(player.id.equals(playerID)){ 
                return player;
            }
        }
        return null;
    }
 
    public Player getPlayerByName(String playerName){
        return players.get(playerName);
    }

    public Card getPlayAreaCardByID(UUID itemID){
        for (Player player : players.values()) {
            for (Card item : player.playArea){
                if(item.id.equals(itemID)){ 
                    return item;
                }
            }
        }
        return null;
    }
    
    public void switchOwner(UUID itemID){
        Card item = getPlayAreaCardByID(itemID);
        players.get(item.owner).playArea.remove(item);
        players.get(getOpponentName(item.owner)).playArea.add(item);
        item.owner = getOpponentName(item.owner);
    }
    
    /*
    public void win(String player) {
        Connection connection = connections.get(player);
        connection.send(Message.win());
        connection.closeConnection();
        connection = connections.get(getOpponentName(player));
        connection.send(Message.lose());
        connection.closeConnection();
    }
    
    public void lose(String player) {
        Connection connection = connections.get(player);
        connection.send(Message.lose());
        connection.closeConnection();
        connection = connections.get(getOpponentName(player));
        connection.send(Message.win());
        connection.closeConnection();
    }
    
 
    public ArrayList<Card> orderCards(String username, ArrayList<Card> cards) {
        Connection connection = connections.get(username);
        connection.send(order(cards));
        Message message = connection.receive();
        if (message != null) {
            return sortByID(cards, (ArrayList<UUID>)message.object);
        }
        return null;
    }

    public void loot(String username, int x) {
        draw(username, x);
        discard(username, x);
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** GAME *****\n");
        sb.append("Game UUID: ").append(id).append("\n");
        sb.append("Turn Player: ").append(turnPlayer).append("\n");
        sb.append("Active Player: ").append(activePlayer).append("\n");
        sb.append("TurnCount: ").append(turnCount).append("\n");
        sb.append("Phase: ").append(phase).append("\n");
        sb.append("Pass Count: ").append(passCount).append("\n");
        players.forEach((name, player) -> {
            sb.append(player).append("\n");
        });
        sb.append("****************\n");
        return sb.toString();
    }
    
    
    void processTrigger (Event e) {
        players.get(turnPlayer).addTriggerEvent(this, e);
        players.get(getOpponentName(turnPlayer)).addTriggerEvent(this, e);
    }
    
    
    public void possess(UUID cardID, String newController) {
        Card c = getPlayAreaCardByID(cardID);
        String oldController = c.controller;
        players.get(oldController).playArea.remove(c);
        players.get(newController).playArea.add(c);
        c.controller = newController;
        //processTrigger(Event.possess(oldController, newController, cardID));
    }

    public void discardAfterPlay(Card c) {
        players.get(c.controller).scrapyard.add(c);
    }

    public boolean ownedByOpponent(UUID targetID) {
        Card c = getCard(targetID);
        return c.owner.equals(getOpponentName(c.controller));
    }

    public void addEnergy(String controller, int i) {
        players.get(controller).energy+=i;
    }

    public void removeEnergy(String controller, int i) {
        players.get(controller).energy-=i;
    }

    public void removeFromHand(String controller, UUID id) {
        Player p = players.get(controller);
        p.hand.remove(p.getCard(id));
    }

    public void drawFromVoid(String controller, UUID retID) {
        Player p = players.get(controller);
        Card c = p.getCard(retID);
        p.voidPile.remove(c);
        p.hand.add(c);
        //processTrigger(Event.draw(controller, 1));
    }
    
    
    public void purgeFromHand(String username, int count) {
        Connection connection = connections.get(username);
        connection.send(Message.selectFromHand(toClientGame(username), count));
        Message message = connection.receive();
        if (message != null) {
            Player player = players.get(username);
            player.purgeCardsFromHand((ArrayList<UUID>)message.object);
        }
    }
    
    public void selectFromTopOfDeck(String controller, int numOfCardsToLook, int NumOfCardsToSelect, 
                                    Function<Card, Boolean> isValidTarget, 
                                    BiConsumer<Player, ArrayList<Card>> processSelected,
                                    BiConsumer<Player, ArrayList<Card>> processNotSelected) {
        //TODO: implement this
    }
    */

    public void addConnection(String username, GameEndpoint connection) {
        connections.put(username, connection);
    }

    public void removeConnection(String username) {
            connections.remove(username);
    }

    public Card getCard(UUID targetID) {
        for (Player player : players.values()) {
            Card c = player.getCard(targetID);
            if (c != null){
                return c;
            }
        }
        return null;
    }
    
    public Card peekCard(UUID targetID) {
        for (Player player : players.values()) {
            Card c = player.peekCard(targetID);
            if (c != null){
                return c;
            }
        }
        return null;
    }
    
    public void playCard(String username, UUID cardID) {
        getCard(cardID).play(this);
        activePlayer = getOpponentName(username);
        
    }
    
    public void addToStack(Card c) {
        passCount = 0;
        stack.add(0, c);
    }

    public void studyCard(String username, UUID cardID) {
        getCard(cardID).study(this);
    }
    
    public void changePhase(){
        switch(phase) {
            case MULLIGAN:
                newTurn();
                break;
            case BEGIN:
                passCount = 0;
                activePlayer = turnPlayer;
                phase = MAIN;
                break;
            case MAIN:
                activePlayer = "";
                phase = END;
                break;
            case END:
                newTurn();
                break;
        }
    }
    
    void newTurn(){
        phase = MAIN;
        if(turnCount > 0){
            turnPlayer = getOpponentName(turnPlayer);
            players.get(turnPlayer).draw(1);
        }
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

    public void pass(String username) {
        passCount++;
        if (passCount == 2) {
            if (!stack.isEmpty()){
                resolveStack();
            } else {
                changePhase();
            }
        } else {
            activePlayer = getOpponentName(username);
        }
    }

    private void resolveStack() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mulligan(String username) {
        players.get(username).mulligan();
    }

    public void keep(String username) {
        passCount++;
        if (passCount == 2) {
            changePhase();
        } else {
            activePlayer = getOpponentName(username);
        }
    }

    public GameState toGameState(String username){
        GameState.Builder b = 
                GameState.newBuilder()
                .setId(id.toString())
                .setPlayer(players.get(username).toPlayerMessage())
                .setOpponent(players.get(getOpponentName(username)).toOpponentMessage())
                .setTurnPlayer(turnPlayer)
                .setActivePlayer(activePlayer)
                .setPhase(phase);
        for(int i = 0; i < stack.size(); i++){
            b.addStack(stack.get(i).toCardMessage());
        }
    return b.build();
    }
    
    public void updatePlayers(){
        connections.forEach((p , e) -> {
            try {
                e.send(ServerGameMessage.newBuilder()
                        .setUpdateGameState(UpdateGameState.newBuilder()
                                .setGame(toGameState(p))));
            } catch (IOException | EncodeException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
