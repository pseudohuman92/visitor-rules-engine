
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.properties.Targeting;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.protocol.ServerGameMessages;
import com.ccg.ancientaliens.protocol.ServerGameMessages.Loss;
import com.ccg.ancientaliens.protocol.ServerGameMessages.SelectFromHand;
import com.ccg.ancientaliens.protocol.ServerGameMessages.SelectFromPlay;
import com.ccg.ancientaliens.server.GameEndpoint;

import com.ccg.ancientaliens.protocol.ServerGameMessages.ServerGameMessage;
import com.ccg.ancientaliens.protocol.ServerGameMessages.UpdateGameState;
import com.ccg.ancientaliens.protocol.ServerGameMessages.Win;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.*;
import static com.ccg.ancientaliens.protocol.Types.Phase.*;
import helpers.Hashmap;
import java.io.IOException;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.function.Function;
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
 
    public ArrayList<Card> orderCards(String username, ArrayList<Card> cards) {
        Connection connection = connections.get(username);
        connection.send(order(cards));
        Message message = connection.receive();
        if (message != null) {
            return sortByID(cards, (ArrayList<UUID>)message.object);
        }
        return null;
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

    public Card extractCard(UUID targetID) {
        for (Player player : players.values()) {
            Card c = player.extractCard(targetID);
            if (c != null){
                return c;
            }
        }
        return null;
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
    
    public void playCard(String username, UUID cardID) {
        extractCard(cardID).play(this);
        activePlayer = getOpponentName(username);
        
    }
    
    public void addToStack(Card c) {
        passCount = 0;
        stack.add(0, c);
    }

    public void studyCard(String username, UUID cardID) {
        extractCard(cardID).study(this);
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
                passCount = 0;
                activePlayer = turnPlayer;
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
        if (passCount == 2) {
            activePlayer = "";
            Card c = stack.remove(0);
            c.resolve(this);
            updatePlayers();
            if (!stack.isEmpty()){
                resolveStack();
            } else {
                passCount = 0;
                activePlayer = turnPlayer;
            }
        }
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
    
    public boolean hasValidTargetsInPlay(Targeting c, int count){
        for (Player player : players.values()) {
            for (Card cx : player.playArea){
                if (c.validTarget(cx)){
                    count--;
                    if (count == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void getTargetsFromPlay(Card c, int count){        
        SelectFromPlay.Builder b = SelectFromPlay.newBuilder()
                .setSelectionCount(count)
                .setGame(toGameState(c.controller));
        for (Player player : players.values()) {
            for (Card cx : player.playArea){
                if (((Targeting)c).validTarget(cx)){
                    b.addCandidates(cx.toCardMessage());
                }
            }
        }
        try {
            connections.get(c.controller).sendForResponse(
                    ServerGameMessage.newBuilder().setSelectFromPlay(b),
                    (l) -> { c.supplementaryData = l;
                             c.supplementaryData.notify();});
            c.supplementaryData.wait();
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void possess(UUID cardID, String newController) {
        Card c = extractCard(cardID);
        players.get(newController).playArea.add(c);
        c.controller = newController;
    }
    
    public void putToScrapyard(Card c) {
        players.get(c.controller).scrapyard.add(c);
    }
    
    public void addEnergy(String controller, int i) {
        players.get(controller).energy+=i;
    }
    
    public void spendEnergy(String controller, int i) {
        players.get(controller).energy-=i;
    }
    
    public void draw(String username, int count){
        Player player = players.get(username);
        player.draw(count);
        if (player.deck.deck.isEmpty()){
            lose(username);
        }
    }

    public void destroy(UUID id){
        Card item = extractCard(id);
        players.get(item.controller).scrapyard.add(item);
    }
    
    public void loot(String username, int x) {
        draw(username, x);
        discard(username, x);
    }
    
    public void discard(String username, int count){
        Player p = players.get(username);
        SelectFromHand.Builder b = SelectFromHand.newBuilder()
                .setSelectionCount(count)
                .setGame(toGameState(username));
        p.hand.forEach(c -> {
            b.addCandidates(c.toCardMessage());
        });
        try {
            connections.get(username).sendForResponse(
                    ServerGameMessage.newBuilder().setSelectFromHand(b),
                    (l) -> { p.discard((ArrayList<UUID>)l);
                             p.notify();});
            p.wait();
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deplete(UUID id){
        getCard(id).depleted = true;
    }
    
    public void ready(UUID id){
        getCard(id).depleted = false;
    }
    
    public boolean ownedByOpponent(UUID targetID) {
        Card c = getCard(targetID);
        return c.owner.equals(getOpponentName(c.controller));
    }
    
    public void purge(String username, int count){
        Player player = players.get(username);
        player.purgeFromDeck(count);
        if (player.deck.deck.isEmpty()){
            lose(username);
        }
    }

    public void win(String player) {
        try {
            GameEndpoint connection = connections.get(player);
            connection.send(ServerGameMessage.newBuilder().setWin(Win.newBuilder()));
            connection = connections.get(getOpponentName(player));
            connection.send(ServerGameMessage.newBuilder().setLoss(Loss.newBuilder()));
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void lose(String player) {
        try {
            GameEndpoint connection = connections.get(player);
            connection.send(ServerGameMessage.newBuilder().setLoss(Loss.newBuilder()));
            connection = connections.get(getOpponentName(player));
            connection.send(ServerGameMessage.newBuilder().setWin(Win.newBuilder()));
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
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
