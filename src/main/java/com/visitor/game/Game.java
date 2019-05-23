
package com.visitor.game;

import com.visitor.card.properties.Activatable;
import com.visitor.card.properties.Damageable;
import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Card;
import com.visitor.card.types.Junk;
import com.visitor.helpers.UUIDHelper;
import com.visitor.protocol.ServerGameMessages.*;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.GameState;
import com.visitor.protocol.Types.Phase;
import com.visitor.server.GameEndpoint;
import static com.visitor.protocol.Types.Phase.*;
import com.visitor.protocol.Types.SelectFromType;
import static com.visitor.protocol.Types.SelectFromType.*;
import com.visitor.helpers.Hashmap;
import java.io.IOException;
import static java.lang.Math.random;
import com.visitor.helpers.Arraylist;
import com.visitor.server.GeneralEndpoint;
import java.util.List;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.concurrent.ArrayBlockingQueue;
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
    Hashmap<String, ServerGameMessage> lastMessages;
    String turnPlayer;
    public String activePlayer;
    Arraylist<Card> stack;
    Phase phase;
    int turnCount;
    int passCount;
    UUID id;
    ArrayBlockingQueue<Object> response;
    Hashmap<String, Arraylist<Triggering>> triggeringCards;
    Arraylist<Event> eventQueue;
    boolean endProcessed;

    public Game (Player p1, Player p2) {
        id = randomUUID();
        players = new Hashmap<>();
        connections = new Hashmap<>();
        stack = new Arraylist<>();
        lastMessages = new Hashmap<>();
        response = new ArrayBlockingQueue<>(1);
        triggeringCards = new Hashmap<>();
        triggeringCards.put(p1.userId, new Arraylist<>());
        triggeringCards.put(p2.userId, new Arraylist<>());
        eventQueue = new Arraylist();
        
        players.put(p1.userId, p1);
        players.put(p2.userId, p2);

        p1.deck.shuffle();
        p2.deck.shuffle();
        
        phase = MULLIGAN;
        turnPlayer = (random() < 0.5)?p1.userId:p2.userId;
        activePlayer = turnPlayer;
        turnCount = 0;
        passCount = 0;
        p1.draw(5);
        p2.draw(5);
        updatePlayers();
    }

    public void addConnection(String userId, GameEndpoint connection) {
        connections.putIn(userId, connection);
    }

    public void removeConnection(String userId) {
        connections.removeFrom(userId);
    }

    public void setLastMessage(String userId, ServerGameMessage lastMessage) {
        lastMessages.put(userId, lastMessage);
    }

    public ServerGameMessage getLastMessage(String userId) {
        return lastMessages.get(userId);
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
    
    public void playCard(String userId, UUID cardID) {
        extractCard(cardID).play(this);
        activePlayer = getOpponentName(userId); 
    }
    
    public void activateCard(String userId, UUID cardID) {
        ((Activatable)getCard(cardID)).activate(this);
        activePlayer = getOpponentName(userId); 
    }
    
    public void addToStack(Card c) {
        passCount = 0;
        stack.add(0, c);
    }

    public void studyCard(String userId, UUID cardID) {
        extractCard(cardID).study(this);
    }
    
    public void processEvents(){
        while(!eventQueue.isEmpty()){
            Event e = eventQueue.remove(0);
            System.out.println("Processing Event: " + e.label);
            triggeringCards.get(turnPlayer).forEachInOrder(c ->{ c.checkEvent(this, e);});
            triggeringCards.get(getOpponentName(turnPlayer)).forEachInOrder(c ->{ c.checkEvent(this, e);});
        }
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
                activePlayer = " ";
                updatePlayers();
                endTurn();
                updatePlayers();
                newTurn();
                updatePlayers();
                break;
            case END:
                break;
        }
    }
    
    private void endTurn() {
        if (!endProcessed){
            endProcessed = true;
            processEndEvents();
            resolveStack(); //TODO: figure out logic here
        }
        players.values().forEach(p->{ p.shield = 0; p.reflect = 0;});
        if(players.get(turnPlayer).hand.size() > 7){
            discard(turnPlayer, players.get(turnPlayer).hand.size()-7);
            
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
        processBeginEvents();
    }
    

    public String getOpponentName(String playerName){
        for(String name : players.keySet()){
            if(!name.equals(playerName)){
                return name;
            }
        }
        return null;
    }

    public void pass(String userId) {
        passCount++;
        if (passCount == 2) {
            if (!stack.isEmpty()){
                resolveStack();
            } else {
                changePhase();
            }
        } else {
            activePlayer = getOpponentName(userId);
        }
    }

    
    //This is resolve until something new is added version
    private void resolveStack() {
        activePlayer = " ";
        updatePlayers();    
        while (!stack.isEmpty() && passCount == 2) {
            Card c = stack.remove(0);
            c.resolve(this);
            int prevSize = stack.size();
            processEvents();
            if(stack.isEmpty() || prevSize != stack.size()){
                passCount = 0;
                activePlayer = turnPlayer;
            } else {
                updatePlayers();
            }
        }
    }
    
    /*
    // This is stop after each resolution version.
    private void resolveStack() {
        if (passCount == 2) {
            activePlayer = " ";
            updatePlayers();
            Card c = stack.remove(0);
            c.resolve(this);
            passCount = 0;
            activePlayer = turnPlayer;
        }
    }
    */

    public void redraw(String userId) {
        players.get(userId).redraw();
    }

    public void keep(String userId) {
        passCount++;
        if (passCount == 2) {
            changePhase();
        } else {
            activePlayer = getOpponentName(userId);
        }
    }
    
    //Eventually make this private.
    public Arraylist<Card> getZone(String userId, String zone){
        switch(zone){
            case "deck":
                return players.get(userId).deck;
            case "hand":
                return players.get(userId).hand;
            case "play":
                return players.get(userId).playArea;
            case "scrapyard":
                return players.get(userId).scrapyard;
            case "void":
                return players.get(userId).voidPile;
            case "stack":
                return stack;
            case "both play":
                Arraylist<Card> total = new Arraylist<>();
                total.addAll(players.get(userId).playArea);
                total.addAll(players.get(getOpponentName(userId)).playArea);
                return total;
            default:
                return null;
        }
    }
    
    public boolean hasValidTargetsIn(String userId, Predicate<Card> validTarget, int count, String zone){
        return getZone(userId, zone).parallelStream().filter(validTarget).count() >= count;
    }
    
     public boolean hasCardsIn(String userId, String zone, int count) {
        return getZone(userId, zone).size() >= count;
    }
    
    public void putTo(String userId, Card c, String zone) {
        getZone(userId, zone).add(c);
    }
    
    public void putTo(String userId, Card c, String zone, int index) {
        getZone(userId, zone).add(index, c);
    }
    
    public void addEnergy(String userId, int i) {
        players.get(userId).energy+=i;
    }
    
    public void spendEnergy(String userId, int i) {
        players.get(userId).energy-=i;
    }
    
    public void draw(String userId, int count){
        Player player = players.get(userId);
        player.draw(count);
        if (player.deck.isEmpty()){
            lose(userId);
        }
    }
    
    public void drawByID(String userId, UUID cardID) {
        players.get(userId).hand.add(extractCard(id));
    }
    
    public void purge(String userId, UUID cardID) {
        players.get(userId).voidPile.add(extractCard(id));
    }

    public void destroy(UUID id){
        Card item = extractCard(id);
        item.destroy(this);
    }
    
    public void loot(String userId, int x) {
        draw(userId, x);
        discard(userId, x);
    }
    
    public void discard(String userId, int count){
        players.get(userId).discard(selectFromZone(userId, "hand", c -> {return true;}, count, false));
    }
    
    public void discard(String userId, UUID cardID){
        Arraylist<UUID> temp = new Arraylist<>();
        temp.add(cardID);
        players.get(userId).discard(temp);
    }
    
    public void deplete(UUID id){
        getCard(id).depleted = true;
    }
    
    public void ready(UUID id){
        getCard(id).ready();
    }
    
    public boolean ownedByOpponent(UUID targetID) {
        Card c = getCard(targetID);
        return c.owner.equals(getOpponentName(c.controller));
    }
    
    public void damagePlayer(String userId, int count){
        String current = userId;
        Player player; 
        int ret = count;
        
        do {
            player = players.get(current);
            ret = player.dealDamage(this, ret);
            if (player.health <= 0){
                lose(current);
            }
            current = getOpponentName(current);
        } while(ret > 0);
    }
    
    public void payLife(String userId, int count){
        Player player = players.get(userId);
        player.payLife(count);
        if (player.health <= 0){
            lose(userId);
        }
    }

    public void possessTo(String newController, UUID cardID, String zone) {
        Card c = extractCard(cardID);
        c.controller = newController;
        c.knowledge = new Hashmap<>();
        getZone(newController, zone).add(c);
    }

    public boolean controlsUnownedCard(String userId, String zone) {
        return getZone(userId, zone).parallelStream().anyMatch(c->{return ownedByOpponent(c.id);});
    }

    public boolean isIn(String userId, UUID cardID, String zone) {
        return getZone(userId, zone).parallelStream().anyMatch(getCard(cardID)::equals);
    }
    
    public boolean hasInstancesIn(String userId, Class c, String zone, int count) {
        return getZone(userId, zone).parallelStream().filter(c::isInstance).count() >= count;
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

    public Arraylist<Card> extractAllCopiesFrom(String userId, String cardName, String zone) {
        Arraylist<Card> cards = new Arraylist<>(getZone(userId, zone).parallelStream()
                .filter(c -> { return c.name.equals(cardName);}).collect(Collectors.toList()));
        getZone(userId, zone).removeAll(cards);
        return cards;
    }

    public void putTo(String userId, Arraylist<Card> cards, String zone) {
        getZone(userId, zone).addAll(cards);
    }
    
    public void transformToJunk(UUID cardID){
        Card c = getCard(cardID);
        Junk j = new Junk(c.controller);
        j.copyPropertiesFrom(c);
        replaceWith(c, j);
    }

    public Arraylist<Card> extractAll(List<UUID> list) {
        return new Arraylist<>(list.stream().map(i -> {return extractCard(i);}).collect(Collectors.toList()));
    }
    
    public void shuffleIntoDeck(String userId, Arraylist<Card> cards) {
        players.get(userId).deck.shuffleInto(cards);
    }

    private SelectFromType getZoneLabel(String zone){
        switch(zone){
            case "hand":
                return HAND;
            case "both play":
            case "play":
                return PLAY;
            case "scrapyard":
                return SCRAPYARD;
            case "void":
                return VOID;
            case "stack":
                return STACK;
            default:
                return NOTYPE;
        }
    }
    
    public int selectX(String userId, int maxX) {
        if (maxX == 0){
            return maxX;
        }
        SelectXValue.Builder b = SelectXValue.newBuilder()
                .setMaxXValue(maxX)
                .setGame(toGameState(userId));
        try {
            send(userId, ServerGameMessage.newBuilder().setSelectXValue(b));
            
            int l = (int)response.take();
            return l;
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private Arraylist<UUID> selectFrom(String userId, SelectFromType type, Arraylist<Card> candidates, Arraylist<UUID> canSelect, Arraylist<UUID> canSelectPlayers, int count, boolean upTo){
        SelectFrom.Builder b = SelectFrom.newBuilder()
                .addAllCanSelected(canSelect.parallelStream().map(u->{return u.toString();}).collect(Collectors.toList()))
                .addAllCanSelectedPlayers(canSelectPlayers.parallelStream().map(u->{return u.toString();}).collect(Collectors.toList()))
                .addAllCandidates(candidates.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .setMessageType(type)
                .setSelectionCount(count)
                .setUpTo(upTo)
                .setGame(toGameState(userId));
        try {
            send(userId, ServerGameMessage.newBuilder().setSelectFrom(b));
            System.out.println("Waiting targets!");
            String[] l = (String[])response.take();
            System.out.println("Done waiting!");
            return UUIDHelper.toUUIDList(l);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Arraylist<UUID> selectFromZone(String userId, String zone, Predicate<Card> validTarget, int count, boolean upTo) {        
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(userId, zone).parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(userId, getZoneLabel(zone), getZone(userId, zone), canSelect, new Arraylist<>(), count, upTo);
    }
    
    public Arraylist<UUID> selectFromZoneWithPlayers(String userId, String zone, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo) {        
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(userId, zone).parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList()));
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(userId, getZoneLabel(zone), getZone(userId, zone), canSelect, canSelectPlayers, count, upTo);
    }

    public Arraylist<UUID> selectFromList(String userId, Arraylist<Card> candidates, Predicate<Card> validTarget, int count, boolean upTo) {
        Arraylist<UUID> canSelect = new Arraylist<>(candidates.parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(userId, LIST, candidates, canSelect, new Arraylist<>(), count, upTo);
    }
    
    public Arraylist<UUID> selectPlayers(String userId, Predicate<Player> validPlayer, int count, boolean upTo) {        
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(userId, getZoneLabel("play"), new Arraylist<>(), new Arraylist<>(), canSelectPlayers, count, upTo);
    }
    
    public Arraylist<UUID> selectDamageTargetsConditional(String userId, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo) {        
        return selectFromZoneWithPlayers(userId, "both play", validTarget, validPlayer, count, upTo);
    }
    
    public Arraylist<UUID> selectDamageTargets(String userId, int count, boolean upTo) {        
        return selectFromZoneWithPlayers(userId, "both play", c->{return c instanceof Damageable;}, p->{return true;}, count, upTo);
    }
    
    
    
    public void win(String player) {
        send(player, ServerGameMessage.newBuilder().setGameEnd(GameEnd.newBuilder().setGame(toGameState(player)).setWin(true)));
        send(getOpponentName(player), ServerGameMessage.newBuilder().setGameEnd(GameEnd.newBuilder().setGame(toGameState(getOpponentName(player))).setWin(false)));
        connections.forEach((s, c) -> {c.close();});
        connections = new Hashmap<>();
        GeneralEndpoint.gameServer.removeGame(id);
    }
    
    
    public void lose(String player) {
        send(player, ServerGameMessage.newBuilder().setGameEnd(GameEnd.newBuilder().setGame(toGameState(player)).setWin(false)));
        send(getOpponentName(player), ServerGameMessage.newBuilder().setGameEnd(GameEnd.newBuilder().setGame(toGameState(getOpponentName(player))).setWin(true)));
        connections.forEach((s, c) -> {c.close();});
        connections = new Hashmap<>();
        GeneralEndpoint.gameServer.removeGame(id);
    }
    
    public GameState.Builder toGameState(String userId){
        GameState.Builder b = 
                GameState.newBuilder()
                .setId(id.toString())
                .setPlayer(players.get(userId).toPlayerMessage())
                .setOpponent(players.get(getOpponentName(userId)).toOpponentMessage())
                .setTurnPlayer(turnPlayer)
                .setActivePlayer(activePlayer)
                .setPhase(phase);
        for(int i = 0; i < stack.size(); i++){
            b.addStack(stack.get(i).toCardMessage());
        }
        players.forEach((s, p) -> {
            if(userId.equals(s) && isActive(s)){
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
            }
        });
        return b;
    }

    public void send(String userId, ServerGameMessage.Builder builder) {
        try {
            setLastMessage(userId, builder.build());
            GameEndpoint e = connections.get(userId);
            if (e != null) {
                e.send(builder);
            }
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void updatePlayers(){
        players.forEach((name, player) -> {
            send(name, ServerGameMessage.newBuilder()
                    .setUpdateGameState(UpdateGameState.newBuilder()
                            .setGame(toGameState(name))));
        });
    }

    public void addReflect(String userId, int i) {
        players.get(userId).reflect += i;
    }
    
    public void addShield(String userId, int i) {
        players.get(userId).shield += i;
    }

    public boolean hasEnergy(String userId, int i) {
        return players.get(userId).energy >= i;
    }

    public boolean hasKnowledge(String userId, Hashmap<Types.Knowledge, Integer> knowledge) {
         return players.get(userId).hasKnowledge(knowledge);
    }

    public boolean canPlaySlow(String userId) {
         return turnPlayer.equals(userId)
                && activePlayer.equals(userId)
                && stack.isEmpty()
                && phase == MAIN;
    }

    public UUID getId() {
        return id;
    }

    public boolean canStudy(String userId) {
        return canPlaySlow(userId)
            && players.get(userId).numOfStudiesLeft > 0;
    }

    //Eventually get rid of this
    public Player getPlayer(String userId) {
        return players.get(userId);
    }

    public int getEnergy(String controller) {
        return players.get(controller).energy;
    }

    public boolean isActive(String userId) {
        return activePlayer.equals(userId);
    }

    public boolean isAPlayer(String userId) {
        return players.getOrDefault(userId, null) != null;
    }

    public void addToResponseQueue(Object o) {
        try {
            response.put(o);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processBeginEvents() {
        System.out.println("Starting Begin Triggers");
        eventQueue.add(Event.turnStart(turnPlayer));
        processEvents();
        System.out.println("Ending Begin Triggers");
    }

    private void processEndEvents() {
        eventQueue.add(Event.turnEnd(turnPlayer));
        processEvents();
    }

    public void registerTriggeringCard(String userId, Triggering t) {
        triggeringCards.get(userId).add(t);
    }

    public void removeTriggeringCard(Triggering card) {
        triggeringCards.values().forEach(l->{l.remove(card);});
    }

    public String getUserId(UUID targetPlayerId) {
        for (int i = 0; i < players.size(); i++){
            Player p = players.values().toArray(new Player[0])[i];
            if (p.id.equals(targetPlayerId)) {
                return p.userId;
            }
        }
        return "";
    }
    
    public void dealDamage(UUID targetId, int damage){
        String userId = getUserId(targetId);
        if (!userId.isEmpty()){
            damagePlayer(userId, damage);
        } else {
            ((Damageable)getCard(targetId)).dealDamage(this, damage);
        }
    }

    public boolean isTurnPlayer(String userId) {
        return turnPlayer.equals(userId);
    }

    public UUID getOpponentId(String userId) {
        return players.get(getOpponentName(userId)).id;
    }
}
