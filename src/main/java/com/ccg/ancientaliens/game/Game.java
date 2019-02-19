
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.properties.Activatable;
import com.ccg.ancientaliens.card.properties.Targeting;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.helpers.UUIDHelper;
import com.ccg.ancientaliens.protocol.ServerGameMessages.*;
import com.ccg.ancientaliens.server.GameEndpoint;
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
        
        players.get(p1).deck.shuffle();
        players.get(p2).deck.shuffle();
        
        phase = MULLIGAN;
        turnPlayer = (random() < 0.5)?p1:p2;
        activePlayer = turnPlayer;
        turnCount = 0;
        passCount = 0;
        players.get(p1).draw(5);
        players.get(p2).draw(5);
    }

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
    
    public void activateCard(String username, UUID cardID) {
        ((Activatable)getCard(cardID)).activate(this);
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
    
    private void newTurn(){
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
    
     public boolean hasACardInVoid(String username) {
        return !players.get(username).voidPile.isEmpty();
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
    
    public void drawByID(String username, UUID cardID) {
        players.get(username).hand.add(extractCard(id));
    }
    
    public void purgeByID(String username, UUID cardID) {
        players.get(username).voidPile.add(extractCard(id));
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
        players.get(username).discard(getSelectedFromHand(username, (c -> {return true;}), count));
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
    
    public GameState.Builder toGameState(String username){
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
        players.forEach((s, p) -> {
            p.hand.forEach(c -> {
                if(c.canPlay(this)){
                    b.addCanPlay(c.id.toString());
                }
                if(c.canStudy(this)){
                    b.addCanStudy(c.id.toString());
                }
            });
            p.playArea.forEach(c -> {
                if(c instanceof Activatable && ((Activatable)c).canActivate(this)){
                    b.addCanActivate(c.id.toString());
                }
            });
        });
        return b;
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
    
    public ArrayList<UUID> getSelectedFromPlay(String username, Function<Card, Boolean> validTarget, int count){        
        SelectFromPlay.Builder b = SelectFromPlay.newBuilder()
                .setSelectionCount(count)
                .setGame(toGameState(username));
        for (Player player : players.values()) {
            for (Card cx : player.playArea){
                if (validTarget.apply(cx)){
                    b.addCandidates(cx.toCardMessage());
                }
            }
        }
        try {
            connections.get(username).sendForResponse(
                    ServerGameMessage.newBuilder().setSelectFromPlay(b));
            System.out.println("Waiting targets!");
            String[] l = (String[])connections.get(username).getResponse();
            System.out.println("Done waiting!");
            return UUIDHelper.toUUIDList(l);
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<UUID> getSelectedFromHand(String username, Function<Card, Boolean> validTarget, int count) {
        Player p = players.get(username);
        SelectFromHand.Builder b = SelectFromHand.newBuilder()
                .setSelectionCount(count)
                .setGame(toGameState(username));
        p.hand.forEach(c -> {
            if(validTarget.apply(c))
                b.addCandidates(c.toCardMessage());
        });
        try {
            connections.get(username).sendForResponse(
                    ServerGameMessage.newBuilder().setSelectFromHand(b));
            
            System.out.println("Waiting discard!");
            String[] l = (String[])connections.get(username).getResponse();
            System.out.println("Done waiting!");
            
            return UUIDHelper.toUUIDList(l);
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getXValue(String username, int maxX) {
        Player p = players.get(username);
        SelectXValue.Builder b = SelectXValue.newBuilder()
                .setMaxXValue(maxX)
                .setGame(toGameState(username));
        try {
            connections.get(username).sendForResponse(
                    ServerGameMessage.newBuilder().setSelectXValue(b));
            
            System.out.println("Waiting discard!");
            int l = (int)connections.get(username).getResponse();
            System.out.println("Done waiting!");
            return l;
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<UUID> getSelectedFromList(String username, ArrayList<Card> candidates, ArrayList<UUID> canSelect, int count) {
        Player p = players.get(username);
        SelectFromList.Builder b = SelectFromList.newBuilder()
                .setSelectionCount(count)
                .setGame(toGameState(username));
        candidates.forEach(c -> {
            b.addCandidates(c.toCardMessage());
        });
        canSelect.forEach(s -> {
            b.addCanSelected(s.toString());
        });
        try {
            connections.get(username).sendForResponse(
                    ServerGameMessage.newBuilder().setSelectFromList(b));
            
            System.out.println("Waiting discard!");
            String[] l = (String[])connections.get(username).getResponse();
            System.out.println("Done waiting!");
            
            return UUIDHelper.toUUIDList(l);
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<UUID> getSelectedFromVoid(String username, Function<Card, Boolean> validTarget, int count) {
        Player p = players.get(username);
        SelectFromVoid.Builder b = SelectFromVoid.newBuilder()
                .setSelectionCount(count)
                .setGame(toGameState(username));
        p.voidPile.forEach(c -> {
            if(validTarget.apply(c))
                b.addCandidates(c.toCardMessage());
        });
        try {
            connections.get(username).sendForResponse(
                    ServerGameMessage.newBuilder().setSelectFromVoid(b));
            
            System.out.println("Waiting discard!");
            String[] l = (String[])connections.get(username).getResponse();
            System.out.println("Done waiting!");
            
            return UUIDHelper.toUUIDList(l);
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
