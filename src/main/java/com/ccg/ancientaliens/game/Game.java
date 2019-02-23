
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.properties.Activatable;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Junk;
import com.ccg.ancientaliens.helpers.UUIDHelper;
import com.ccg.ancientaliens.protocol.ServerGameMessages.*;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.GameState;
import com.ccg.ancientaliens.protocol.Types.Phase;
import com.ccg.ancientaliens.server.GameEndpoint;
import static com.ccg.ancientaliens.protocol.Types.Phase.*;
import com.ccg.ancientaliens.protocol.Types.SelectFromType;
import static com.ccg.ancientaliens.protocol.Types.SelectFromType.*;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.io.IOException;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.websocket.EncodeException;

/**
 *
 * @author pseudo
 */
public class Game {
    
    Hashmap<String, Player> players;
    Hashmap<String, GameEndpoint> connections;
    String turnPlayer;
    String activePlayer;
    ArrayList<Card> stack;
    Phase phase;
    int turnCount;
    int passCount;
    UUID id;
    
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
        
        players.put(p1, new Player(p1, TestDecks.blackDeck(p1)));
        players.put(p2, new Player(p2, TestDecks.blueDeck(p2)));
        
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
        for (Card c : stack){
            if (c.id.equals(targetID)){
                stack.remove(c);
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
        for (Card c : stack){
            if (c.id.equals(targetID)){
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
        players.values().forEach(p->{ p.shield = 0; p.reflect = 0;});
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

    /*
    //This is resolve until something new is added version
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
    */
    
    // This is stop after each resolution version.
    private void resolveStack() {
        if (passCount == 2) {
            activePlayer = "";
            Card c = stack.remove(0);
            c.resolve(this);
            passCount = 0;
            activePlayer = turnPlayer;
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
    
    //Eventually make this private.
    public ArrayList<Card> getZone(String username, String zone){
        switch(zone){
            case "deck":
                return players.get(username).deck;
            case "hand":
                return players.get(username).hand;
            case "play":
                return players.get(username).playArea;
            case "scrapyard":
                return players.get(username).scrapyard;
            case "void":
                return players.get(username).voidPile;
            case "stack":
                return stack;
            case "both play":
                ArrayList<Card> total = new ArrayList<>();
                total.addAll(players.get(username).playArea);
                total.addAll(players.get(getOpponentName(username)).playArea);
                return total;
            default:
                return null;
        }
    }
    
    public boolean hasValidTargetsIn(String username, Predicate<Card> validTarget, int count, String zone){
        return getZone(username, zone).parallelStream().filter(validTarget).count() >= count;
    }
    
     public boolean hasACardIn(String username, String zone) {
         return !getZone(username, zone).isEmpty();
    }
    
    public void putTo(String username, Card c, String zone) {
        getZone(username, zone).add(c);
    }
    
    public void addEnergy(String username, int i) {
        players.get(username).energy+=i;
    }
    
    public void spendEnergy(String username, int i) {
        players.get(username).energy-=i;
    }
    
    public void draw(String username, int count){
        Player player = players.get(username);
        player.draw(count);
        if (player.deck.isEmpty()){
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
        players.get(username).discard(selectFromHand(username, (c -> {return true;}), count));
    }
    
    public void discard(String username, UUID cardID){
        ArrayList<UUID> temp = new ArrayList<>();
        temp.add(cardID);
        players.get(username).discard(temp);
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
        String current = username;
        Player player; 
        int ret = count;
        
        do {
            player = players.get(current);
            ret = player.purgeFromDeck(ret);
            if (player.deck.isEmpty()){
                lose(current);
            }
            current = getOpponentName(current);
        } while(ret > 0);
    }

    public void possessTo(String newController, UUID cardID, String zone) {
        Card c = extractCard(cardID);
        c.controller = newController;
        c.knowledge = new Hashmap<>();
        getZone(newController, zone).add(c);
    }

    public boolean controlsUnownedCard(String username, String zone) {
        return getZone(username, zone).parallelStream().anyMatch(c->{return ownedByOpponent(c.id);});
    }

    public boolean isIn(String username, UUID cardID, String zone) {
        return getZone(username, zone).parallelStream().anyMatch(getCard(cardID)::equals);
    }
    
    public boolean hasAnInstanceIn(String username, Class c, String zone) {
        return getZone(username, zone).parallelStream().anyMatch(c::isInstance);
    }

    public void replaceWith(Card oldCard, Card newCard) {
        players.values().forEach(p->{p.replaceWith(oldCard, newCard);});
        for (int i = 0; i < stack.size(); i++){
            if(stack.get(i).equals(oldCard)){
                stack.remove(i);
                stack.add(i, newCard);
            }
        }
    }

    public ArrayList<Card> extractAllCopiesFrom(String username, String cardName, String zone) {
        ArrayList<Card> cards = new ArrayList<>(getZone(username, zone).parallelStream()
                .filter(c -> { return c.name.equals(cardName);}).collect(Collectors.toList()));
        getZone(username, zone).removeAll(cards);
        return cards;
    }

    public void putAllTo(String username, ArrayList<Card> cards, String zone) {
        getZone(username, zone).addAll(cards);
    }
    
    public void transformToJunk(UUID cardID){
        Card c = getCard(cardID);
        Junk j = new Junk(c.controller);
        j.copyPropertiesFrom(c);
        replaceWith(c, j);
    }

    public ArrayList<Card> extractAll(List<UUID> list) {
        return new ArrayList<>(list.stream().map(i -> {return extractCard(i);}).collect(Collectors.toList()));
    }
    
    public void shuffleIntoDeck(String username, ArrayList<Card> cards) {
        players.get(username).deck.shuffleInto(cards);
    }

    
    
    
    
    
    public int selectX(String username, int maxX) {
        if (maxX == 0){
            return maxX;
        }
        SelectXValue.Builder b = SelectXValue.newBuilder()
                .setMaxXValue(maxX)
                .setGame(toGameState(username));
        try {
            connections.get(username).send(
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
    
    public ArrayList<UUID> selectFrom(String username, SelectFromType type, ArrayList<Card> candidates, ArrayList<UUID> canSelect, int count){
        if (canSelect.size() == count || (canSelect.size() < count && type == LISTUPTO)){
            return canSelect;
        }
        SelectFrom.Builder b = SelectFrom.newBuilder()
                .addAllCanSelected(canSelect.parallelStream().map(u->{return u.toString();}).collect(Collectors.toList()))
                .addAllCandidates(candidates.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .setMessageType(type)
                .setSelectionCount(count)
                .setGame(toGameState(username));
        try {
            connections.get(username).send(
                    ServerGameMessage.newBuilder().setSelectFrom(b));
            System.out.println("Waiting targets!");
            String[] l = (String[])connections.get(username).getResponse();
            System.out.println("Done waiting!");
            return UUIDHelper.toUUIDList(l);
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<UUID> selectFromPlay(String username, Predicate<Card> validTarget, int count){        
        ArrayList<UUID> canSelect = new ArrayList<>();
        for (Player player : players.values()) {
            canSelect.addAll(player.playArea.parallelStream()
                    .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList()));
        } 
        return selectFrom(username, PLAY, null, canSelect, count);
    }

    public ArrayList<UUID> selectFromHand(String username, Predicate<Card> validTarget, int count) {
        ArrayList<UUID> canSelect = new ArrayList<>();
        canSelect.addAll(players.get(username).hand.parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList())); 
        return selectFrom(username, HAND, null, canSelect, count);
    }
    
     public ArrayList<UUID> selectFromScrapyard(String username, Predicate<Card> validTarget, int count) {
        ArrayList<UUID> canSelect = new ArrayList<>();
        canSelect.addAll(players.get(username).scrapyard.parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList())); 
        return selectFrom(username, SCRAPYARD, null, canSelect, count);
    }
    
    public ArrayList<UUID> selectFromVoid(String username, Predicate<Card> validTarget, int count) {
        ArrayList<UUID> canSelect = new ArrayList<>();
        canSelect.addAll(players.get(username).voidPile.parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList())); 
        return selectFrom(username, VOID, null, canSelect, count);
    }
    
    public ArrayList<UUID> selectFromStack(String username, Predicate<Card> validTarget, int count) {
        ArrayList<UUID> canSelect = new ArrayList<>();
        canSelect.addAll(stack.parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList())); 
        return selectFrom(username, STACK, null, canSelect, count);
    }

    public ArrayList<UUID> selectFromList(String username, ArrayList<Card> candidates, ArrayList<UUID> canSelect, int count) {
        return selectFrom(username, LIST, candidates, canSelect, count);
    }
    
    public ArrayList<UUID> selectFromListUpTo(String username, ArrayList<Card> candidates, ArrayList<UUID> canSelect, int count) {
        return selectFrom(username, LISTUPTO, candidates, canSelect, count);
    }
    
    public String selectPlayer(String username) {
        SelectPlayer.Builder b = SelectPlayer.newBuilder()
                .setGame(toGameState(username));
        try {
            connections.get(username).send(
                    ServerGameMessage.newBuilder().setSelectPlayer(b));
            System.out.println("Waiting targets!");
            String selection = (String)connections.get(username).getResponse();
            System.out.println("Done waiting!");
            return selection;
        } catch (IOException | EncodeException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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

    public void addReflect(String username, int i) {
        players.get(username).reflect += i;
    }
    
    public void addShield(String username, int i) {
        players.get(username).shield += i;
    }

    public boolean hasEnergy(String username, int i) {
        return players.get(username).energy >= i;
    }

    public boolean hasKnowledge(String username, Hashmap<Types.Knowledge, Integer> knowledge) {
         return players.get(username).hasKnowledge(knowledge);
    }

    public boolean canPlaySlow(String username) {
         return turnPlayer.equals(username)
                && activePlayer.equals(username)
                && stack.isEmpty()
                && phase == MAIN;
    }

    public UUID getId() {
        return id;
    }

    public boolean canStudy(String username) {
        return activePlayer.equals(username) 
            && players.get(username).numOfStudiesLeft > 0 
            && stack.isEmpty();
    }

    //Eventually get rid of this
    public Player getPlayer(String username) {
        return players.get(username);
    }
}
