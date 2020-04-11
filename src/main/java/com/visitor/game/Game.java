
package com.visitor.game;

import com.visitor.card.properties.Activatable;
import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Card;
import com.visitor.card.types.Junk;
import com.visitor.card.types.Unit;
import static com.visitor.game.Event.turnEnd;
import static com.visitor.game.Event.turnStart;
import static com.visitor.game.Game.Zone.*;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.helpers.UUIDHelper.toUUIDList;

import com.visitor.protocol.ServerGameMessages;
import com.visitor.protocol.ServerGameMessages.GameEnd;
import com.visitor.protocol.ServerGameMessages.SelectFrom;
import com.visitor.protocol.ServerGameMessages.SelectXValue;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.ServerGameMessages.UpdateGameState;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.GameState;
import com.visitor.protocol.Types.Phase;
import static com.visitor.protocol.Types.Phase.*;
import com.visitor.protocol.Types.SelectFromType;
import static com.visitor.protocol.Types.SelectFromType.*;
import com.visitor.server.GameEndpoint;
import static com.visitor.server.GeneralEndpoint.gameServer;

import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.random;
import static java.lang.System.out;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Predicate;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import java.util.stream.Collectors;
import javax.websocket.EncodeException;
import static com.visitor.game.Event.possess;
import com.visitor.protocol.ServerGameMessages.SelectAttackers;
import com.visitor.protocol.ServerGameMessages.SelectBlockers;
import com.visitor.protocol.ServerGameMessages.AssignDamage;
import com.visitor.protocol.Types.Attacker;
import com.visitor.protocol.Types.AttackerAssignment;
import com.visitor.protocol.Types.Blocker;
import com.visitor.protocol.Types.BlockerAssignment;
import com.visitor.protocol.Types.DamageAssignment;



/**
 *
 * @author pseudo
 */
public class Game implements Serializable {

    Hashmap<String, Player> players;
    public transient Hashmap<String, GameEndpoint> connections;
    Hashmap<String, ServerGameMessage> lastMessages;
    String turnPlayer;
    public String activePlayer;
    Arraylist<Card> stack;
    Phase phase;
    int turnCount;
    int passCount;
    UUID id;
    public transient ArrayBlockingQueue<Object> response;
    
    Hashmap<String, Arraylist<Triggering>> triggeringCards;
    Arraylist<Event> eventQueue;
    boolean endProcessed;
    
    Arraylist<UUID> attackers;
    Arraylist<UUID> blockers;

    public Game (Player p1, Player p2) {
        id = randomUUID();
        players = new Hashmap<>();
        connections = new Hashmap<>();
        stack = new Arraylist<>();
        lastMessages = new Hashmap<>();
        response = new ArrayBlockingQueue<>(1);
        triggeringCards = new Hashmap<>();
        triggeringCards.put(p1.username, new Arraylist<>());
        triggeringCards.put(p2.username, new Arraylist<>());
        eventQueue = new Arraylist<>();
        attackers = new Arraylist<>();
        blockers = new Arraylist<>();
        
        players.put(p1.username, p1);
        players.put(p2.username, p2);

        p1.deck.shuffle();
        p2.deck.shuffle();
        
        phase = REDRAW;
        turnPlayer = (random() < 0.5)?p1.username:p2.username;
        activePlayer = turnPlayer;
        turnCount = 0;
        passCount = 0;
        p1.draw(5);
        p2.draw(5);
        out.println("Updating players from Game constructor. AP: " + activePlayer);
        updatePlayers();
    }
    public int getMaxEnergy(String username) {
        return players.get(username).maxEnergy;
    }
    public void addStudyCount(String username, int count) {
        players.get(username).numOfStudiesLeft += count;
    }
    public void sacrifice(UUID cardId) {
        getCard(cardId).sacrifice(this);
    }

    
    // Connection methods
    public void addConnection(String username, GameEndpoint connection) {
        connections.putIn(username, connection);
    }

    public void removeConnection(String username) {
        connections.removeFrom(username);
    }

    public void setLastMessage(String username, ServerGameMessage lastMessage) {
        lastMessages.put(username, lastMessage);
    }

    public ServerGameMessage getLastMessage(String username) {
        return lastMessages.get(username);
    }

    
    // Card accessor methods
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
    
    public Arraylist<Card> getAllFrom(String username, Zone zone, Predicate<Card> pred){
        Arraylist<Card> cards = new Arraylist<>();
        getZone(username, zone).forEach(c -> {
            if (pred.test(c)){
                cards.add(c);
            }
        });
        return cards;
    }
    
    public Arraylist<Card> extractAll(List<UUID> list) {
        return new Arraylist<>(list.stream().map(i -> {return extractCard(i);}).collect(Collectors.toList()));
    }
    
    public Arraylist<Card> getAll(List<UUID> list) {
        return new Arraylist<>(list.stream().map(i -> {return getCard(i);}).collect(Collectors.toList()));
    }
    
    public Arraylist<Card> extractAllCopiesFrom(String username, String cardName, Zone zone) {
        Arraylist<Card> cards = new Arraylist<>(getZone(username, zone).parallelStream()
                .filter(c -> { return c.name.equals(cardName);}).collect(Collectors.toList()));
        getZone(username, zone).removeAll(cards);
        return cards;
    }

    
    // Action methods
    public void playCard(String username, UUID cardID) {
        extractCard(cardID).play(this);
        activePlayer = getOpponentName(username); 
    }
    
    public void activateCard(String username, UUID cardID) {
        ((Activatable)getCard(cardID)).activate(this);
        activePlayer = getOpponentName(username); 
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

    public void addEvent(Event e){
        eventQueue.add(e);
    }
    
    public void addEventThenProcess(Event e){
        eventQueue.add(e);
        processEvents();    
    }
    
    public void studyCard(String username, UUID cardID) {
        Card c = extractCard(cardID);
        c.study(this, true);
        addEventThenProcess(Event.study(username, c));
    }
    
    public void studyCardIrregular(String username, UUID cardID) {
        Card c = extractCard(cardID);
        c.study(this, false);
        addEvent(Event.study(username, c));   
    }
    
    public void redraw(String username) {
        players.get(username).redraw();
    }

    public void keep(String username) {
        passCount++;
        if (passCount == 2) {
            changePhase();
        } else {
            activePlayer = getOpponentName(username);
        }
    }
    
    
    // Turn / phase methods
    public void processEvents(){
        if (!eventQueue.isEmpty()){
            Arraylist<Event> tempQueue = eventQueue;
            eventQueue = new Arraylist<>();
            while(!tempQueue.isEmpty()){
                Event e = tempQueue.remove(0);
                out.println("Processing Event: " + e.type);
                triggeringCards.get(turnPlayer).forEachInOrder(c ->{ c.checkEvent(this, e);});
                triggeringCards.get(getOpponentName(turnPlayer)).forEachInOrder(c ->{ c.checkEvent(this, e);});
            }
        }
    }
    
    public void changePhase(){
        out.println("Changing Phase from " + phase);
        passCount = 0;
        activePlayer = turnPlayer;
        switch(phase) {
            case REDRAW:
                phase = BEGIN;
                newTurn();
                break;
            case BEGIN:
                phase = MAIN_BEFORE;
                break;
            case MAIN_BEFORE:
                activePlayer = " ";
                phase = ATTACK;
                chooseAttackers();
                activePlayer = turnPlayer;
                if (attackers.isEmpty()){
                    phase = MAIN_AFTER;
                }
                break;
            case ATTACK:
                activePlayer = " ";
                phase = BLOCK;
                chooseBlockers();
                activePlayer = getOpponentName(turnPlayer);
                break;
            case BLOCK:
                dealCombatDamage();
                unsetAttackers();
                unsetBlockers();
                phase = MAIN_AFTER;
                break;
            case MAIN_AFTER:
                phase = END;
                endTurn();                
                break;
            case END:
                phase = BEGIN;
                newTurn();
                break;
        }
    }
    
    private void endTurn() {
        if (!endProcessed){
            endProcessed = true;
            processEndEvents();
            //TODO: figure out logic here
        }
        players.values().forEach(p->{ p.shield = 0; p.reflect = 0;});
        if(players.get(turnPlayer).hand.size() > 7){
            discard(turnPlayer, players.get(turnPlayer).hand.size()-7);
            
        }
        processCleanupEvents();
    }
    
    private void newTurn(){
        if(turnCount > 0){
            turnPlayer = getOpponentName(turnPlayer);
            players.get(turnPlayer).draw(1);
        }
        activePlayer = turnPlayer;
        passCount = 0;
        players.get(turnPlayer).draw(1);
        players.get(turnPlayer).newTurn();
        players.get(getOpponentName(turnPlayer)).resetShields();
        turnCount++;
        processBeginEvents();
    }
    
    
    // Identifier accessor methods
    public String getOpponentName(String playerName){
        for(String name : players.keySet()){
            if(!name.equals(playerName)){
                return name;
            }
        }
        return null;
    }

    public UUID getOpponentId(String username) {
        return players.get(getOpponentName(username)).id;
    }
    
    
    // Stack methods
    public void addToStack(Card c) {
        passCount = 0;
        stack.add(0, c);
    }
    
    // TODO: switch prevSize check to flag system
    private void resolveStack() {
        activePlayer = " ";
        out.println("Updating players from resolveStack beginning. AP: " + activePlayer);
        updatePlayers();    
        while (!stack.isEmpty() && passCount == 2) {
            Card c = stack.remove(0);
            int prevSize = stack.size();
            c.resolve(this);
            processEvents();
            if(stack.isEmpty() || prevSize != stack.size()){
                passCount = 0;
                activePlayer = turnPlayer;
            } else {
                out.println("Updating players from resolveStack loop. AP: " + activePlayer);
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

    
    //Eventually make this private.
    public Arraylist<Card> getZone(String username, Zone zone){
        switch(zone){
            case DECK:
                return players.get(username).deck;
            case HAND:
                return players.get(username).hand;
            case PLAY:
                return players.get(username).playArea;
            case SCRAPYARD:
                return players.get(username).scrapyard;
            case VOID:
                return players.get(username).voidPile;
            case STACK:
                return stack;
            case BOTH_PLAY:
                Arraylist<Card> total = new Arraylist<>();
                total.addAll(players.get(username).playArea);
                total.addAll(players.get(getOpponentName(username)).playArea);
                return total;
            default:
                return null;
        }
    }
    
    public boolean hasIn(String username, Zone zone, Predicate<Card> validTarget, int count){
        return getZone(username, zone).parallelStream().filter(validTarget).count() >= count;
    }
    
    public void putTo(String username, Card c, Zone zone) {
        getZone(username, zone).add(c);
    }
    
    public void putTo(String username, Card c, Zone zone, int index) {
        getZone(username, zone).add(index, c);
    }
    
    public void putTo(String username, Arraylist<Card> cards, Zone zone) {
        getZone(username, zone).addAll(cards);
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
            gameEnd(username, false);
        }
    }
    
    public void draw(String username, UUID cardID) {
        players.get(username).hand.add(extractCard(cardID));
    }
    
    public void purge(String username, UUID cardID) {
        players.get(username).voidPile.add(extractCard(cardID));
    }

    public void destroy(UUID sourceId, UUID targetId){
        Card c = getCard(targetId);
        addEvent(Event.destroy(getCard(sourceId), c));
        c.destroy(this);
    }
    
    public void loot(String username, int x) {
        Game.this.draw(username, x);
        discard(username, x);
    }
    
    public void discard(String username, int count){
        Arraylist<Card> d = players.get(username).discardAll(selectFromZone(username, Zone.HAND, Predicates::any, count, false));
        d.forEach(c -> {
            addEvent(Event.discard(username, c));
        });
    }
    
    public void discard(String username, UUID cardID){
        players.get(username).discard(cardID);
        addEvent(Event.discard(username, getCard(cardID)));
    }
    
    public void discardAll(String username, Arraylist<Card> cards) {
        players.get(username).discardAll(UUIDHelper.toUUIDList(cards));
        cards.forEach(c -> {
            addEvent(Event.discard(username, c));
        });
    }
    
    public void deplete(UUID id){
        getCard(id).deplete();
    }
    
    public void ready(UUID id){
        getCard(id).ready();
    }
    
    public boolean ownedByOpponent(UUID targetID) {
        Card c = getCard(targetID);
        return c.owner.equals(getOpponentName(c.controller));
    }

    public void payLife(String username, int count){
        Player player = players.get(username);
        player.payLife(count);
        if (player.health <= 0){
            gameEnd(username, false);
        }
    }

    public void possessTo(String newController, UUID cardID, Zone zone) {
        Card c = extractCard(cardID);
        String oldController = c.controller;
        c.controller = newController;
        getZone(newController, zone).add(c);
        addEvent(possess(oldController, newController, c));
    }

    public boolean controlsUnownedCard(String username, Zone zone) {
        return getZone(username, zone).parallelStream().anyMatch(c->{return ownedByOpponent(c.id);});
    }

    public boolean isIn(String username, UUID cardID, Zone zone) {
        return getZone(username, zone).parallelStream().anyMatch(getCard(cardID)::equals);
    }

    private void replaceWith(Card oldCard, Card newCard) {
        players.values().forEach(p->{p.replaceWith(oldCard, newCard);});
        for (int i = 0; i < stack.size(); i++){
            if(stack.get(i).equals(oldCard)){
                stack.remove(i);
                stack.add(i, newCard);
            }
        }
    }

    
    //Transformation methods
    public void transformTo(Card transformingCard, Card transformedCard, Card transformTo){
        replaceWith(transformedCard, transformTo);
        addEvent(Event.transform(transformingCard, transformedCard, transformTo));
    }
    
    public void transformToJunk(Card transformingCard, UUID cardID){
        Card card = getCard(cardID);
        Junk junk = new Junk(card.controller);
        junk.copyPropertiesFrom(card);
        transformTo(transformingCard, card, junk);
    }
    
    

    
    
    public void shuffleIntoDeck(String username, Arraylist<Card> cards) {
        players.get(username).deck.shuffleInto(cards);
    }

    private SelectFromType getZoneLabel(Zone zone){
        switch(zone){
            case HAND:
                return SelectFromType.HAND;
            case BOTH_PLAY:
            case PLAY:
                return SelectFromType.PLAY;
            case SCRAPYARD:
                return SelectFromType.SCRAPYARD;
            case VOID:
                return SelectFromType.VOID;
            case STACK:
                return SelectFromType.STACK;
            default:
                return NOTYPE;
        }
    }
    
    
    //Select methods
    public int selectX(String username, int maxX) {
        if (maxX == 0){
            return maxX;
        }
        SelectXValue.Builder b = SelectXValue.newBuilder()
                .setMaxXValue(maxX)
                .setGame(toGameState(username));
        try {
            send(username, ServerGameMessage.newBuilder().setSelectXValue(b));
            
            int l = (int)response.take();
            return l;
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return 0;
    }
    
    private Arraylist<UUID> selectFrom(String username, SelectFromType type, Arraylist<Card> candidates, Arraylist<UUID> canSelect, Arraylist<UUID> canSelectPlayers, int count, boolean upTo){
        SelectFrom.Builder b = SelectFrom.newBuilder()
                .addAllSelectable(canSelect.parallelStream().map(u->{return u.toString();}).collect(Collectors.toList()))
                .addAllSelectable(canSelectPlayers.parallelStream().map(u->{return u.toString();}).collect(Collectors.toList()))
                .addAllCandidates(candidates.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .setMessageType(type)
                .setSelectionCount(count)
                .setUpTo(upTo)
                .setGame(toGameState(username));
        try {
            send(username, ServerGameMessage.newBuilder().setSelectFrom(b));
            String[] l = (String[])response.take();
            return toUUIDList(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }
    
    public Arraylist<UUID> selectFromZone(String username, Zone zone, Predicate<Card> validTarget, int count, boolean upTo) {        
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(username, zone).parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(username, getZoneLabel(zone), getZone(username, zone), canSelect, new Arraylist<>(), count, upTo);
    }
    
    public Arraylist<UUID> selectFromZoneWithPlayers(String username, Zone zone, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo) {        
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(username, zone).parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList()));
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(username, getZoneLabel(zone), getZone(username, zone), canSelect, canSelectPlayers, count, upTo);
    }

    public Arraylist<UUID> selectFromList(String username, Arraylist<Card> candidates, Predicate<Card> validTarget, int count, boolean upTo) {
        Arraylist<UUID> canSelect = new Arraylist<>(candidates.parallelStream()
                .filter(validTarget).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(username, LIST, candidates, canSelect, new Arraylist<>(), count, upTo);
    }
    
    public Arraylist<UUID> selectPlayers(String username, Predicate<Player> validPlayer, int count, boolean upTo) {        
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(c->{return c.id;}).collect(Collectors.toList()));
        return selectFrom(username, getZoneLabel(Zone.PLAY), new Arraylist<>(), new Arraylist<>(), canSelectPlayers, count, upTo);
    }
    
    public Arraylist<UUID> selectDamageTargetsConditional(String username, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo) {        
        return selectFromZoneWithPlayers(username, BOTH_PLAY, validTarget, validPlayer, count, upTo);
    }
    
    public Arraylist<UUID> selectDamageTargets(String username, int count, boolean upTo) {        
        return selectFromZoneWithPlayers(username, BOTH_PLAY, Predicates::isDamageable, Predicates::any, count, upTo);
    }
    
    
    private Arraylist<AttackerAssignment> selectAttackers(String username){
        
        List<String> attackers = getZone(username, Zone.PLAY).parallelStream()
                                .filter(u-> {return u instanceof Unit && ((Unit)u).canAttack(this);})
                                .map(u->{return u.id.toString();}).collect(Collectors.toList());
        
        if(attackers.isEmpty()){
            return (new Arraylist<>());
        }
        Arraylist<String> targets = new Arraylist<>(getOpponentId(username).toString());
        List<String> allies = getZone(getOpponentName(username), Zone.PLAY).parallelStream()
                                .filter(Predicates::isAlly)
                                .map(u->{return u.id.toString();}).collect(Collectors.toList());
        targets.addAll(allies);
        List<Attacker> attackerList = attackers.parallelStream()
                                    .map(a-> {return Attacker.newBuilder()
                                            .setAttackerId(a)
                                            .addAllPossibleAttackTargets(targets).build();})
                                    .collect(Collectors.toList());
        out.println("Sending Select Attackers Message to " + username);
        SelectAttackers.Builder b = SelectAttackers.newBuilder()
                .addAllPossibleAttackers(attackerList)
                .setGame(toGameState(username));
        try {
            send(username, ServerGameMessage.newBuilder().setSelectAttackers(b));
            AttackerAssignment[] l = (AttackerAssignment[])response.take();
            return new Arraylist<>(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }
    
     private Arraylist<BlockerAssignment> selectBlockers(String username){
        
        List<Unit> potentialBlockers = 
                getZone(username, Zone.PLAY)
                .parallelStream()
                .filter(u-> {return u instanceof Unit;})
                .map(c -> {return ((Unit)c);})
                .collect(Collectors.toList());
        
        if(potentialBlockers.isEmpty()){
            return (new Arraylist<>());
        }
        
        Arraylist<Blocker> blockers = new Arraylist<>();
        potentialBlockers.forEach(pb -> {
            List<String> targets = attackers.parallelStream()
                                   .filter(a -> {return pb.canBlock(this, (Unit)getCard(a));})
                                   .map(u->{return getCard(u).id.toString();})
                                   .collect(Collectors.toList());
            if(!targets.isEmpty()){
                blockers.add(Blocker.newBuilder()
                            .setBlockerId(pb.id.toString())
                            .addAllPossibleBlockTargets(targets)
                            .build());
            }
        });
        
        if(blockers.isEmpty()){
            return (new Arraylist<>());
        }
        
        out.println("Sending Select Blockers Message to " + username);
        SelectBlockers.Builder b = SelectBlockers.newBuilder()
                .addAllPossibleBlockers(blockers)
                .setGame(toGameState(username));
        try {
            send(username, ServerGameMessage.newBuilder().setSelectBlockers(b));
            BlockerAssignment[] l = (BlockerAssignment[])response.take();
            return new Arraylist<>(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    public void gameEnd(String player, boolean win) {
        send(player, ServerGameMessage.newBuilder()
                .setGameEnd(GameEnd.newBuilder()
                        .setGame(toGameState(player))
                        .setWin(win)));
        send(getOpponentName(player), ServerGameMessage.newBuilder()
                .setGameEnd(GameEnd.newBuilder()
                        .setGame(toGameState(getOpponentName(player)))
                        .setWin(!win)));
        connections.forEach((s, c) -> {c.close();});
        connections = new Hashmap<>();
        gameServer.removeGame(id);
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
        
        for(int i = 0; i < attackers.size(); i++){
            b.addAttackers(attackers.get(i).toString());
        }
        
        for(int i = 0; i < blockers.size(); i++){
            b.addBlockers(blockers.get(i).toString());
        }
            
        
        players.forEach((s, p) -> {
            if(username.equals(s) && isActive(s)){
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

    public void send(String username, ServerGameMessage.Builder builder) {
        try {
            setLastMessage(username, builder.build());
            GameEndpoint e = connections.get(username);
            if (e != null) {
                e.send(builder);
            }
        } catch (IOException | EncodeException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
    }
    
    public final void updatePlayers(){
        players.forEach((name, player) -> {
            send(name, ServerGameMessage.newBuilder()
                    .setUpdateGameState(UpdateGameState.newBuilder()
                            .setGame(toGameState(name))));
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
                && (phase == MAIN_BEFORE || phase == MAIN_AFTER);
    }

    public UUID getId() {
        return id;
    }

    public boolean canStudy(String username) {
        return canPlaySlow(username)
            && players.get(username).numOfStudiesLeft > 0;
    }

    //TODO: Eventually get rid of this
    public Player getPlayer(String username) {
        return players.get(username);
    }

    public int getEnergy(String controller) {
        return players.get(controller).energy;
    }

    public boolean isActive(String username) {
        return activePlayer.equals(username);
    }

    public boolean isInGame(String username) {
        return players.getOrDefault(username, null) != null;
    }

    public void addToResponseQueue(Object o) {
        try {
            response.put(o);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
    }

    private void processBeginEvents() {
        //out.println("Starting Begin Triggers");
        addEventThenProcess(turnStart(turnPlayer));
        //out.println("Ending Begin Triggers");
    }

    private void processEndEvents() {
        //out.println("Starting End Triggers");
        addEventThenProcess(turnEnd(turnPlayer));
        //out.println("Ending End Triggers");
    }
    
    private void processCleanupEvents() {
        //out.println("Starting Cleanup Triggers");
        addEventThenProcess(Event.cleanup(turnPlayer));
        //out.println("Ending Cleanup Triggers");
    }

    public void addTriggeringCard(String username, Triggering t) {
        triggeringCards.get(username).add(t);
    }

    public void removeTriggeringCard(Triggering card) {
        triggeringCards.values().forEach(l->{l.remove(card);});
    }

    public String getUsername(UUID playerId) {
        for (int i = 0; i < players.size(); i++){
            Player p = players.values().toArray(new Player[0])[i];
            if (p.id.equals(playerId)) {
                return p.username;
            }
        }
        return "";
    }
    
     public UUID getUserId(String username) {
        return players.get(username).id;
    }
        
    public void dealDamage(UUID sourceId, UUID targetId, int damage){
        String username = getUsername(targetId);
        if (!username.isEmpty()){
             players.get(username).dealDamage(this, damage, sourceId);
        } else {
            Card c = getCard(targetId);
            if (c != null && c.isDamageable()){
                c.dealDamage(this, damage, sourceId);
            }
        }
    }

    public boolean isTurnPlayer(String username) {
        return turnPlayer.equals(username);
    }

    public boolean hasMaxEnergy(String username, int count) {
        return players.get(username).maxEnergy >= count;
    }

    public void removeMaxEnergy(String username, int count) {
        players.get(username).maxEnergy -= count;
    }

    public boolean isPlayer(UUID targetId) {
        return players.values().stream().anyMatch(p -> {
            return p.id.equals(targetId);  
        });
    }

    public void gainHealth(String username, int health) {
        players.get(username).health += health;
    }

    public void dealDamageToAll(String username, UUID sourceId, int damage) {
        players.values().forEach(p -> {
            dealDamage(sourceId, p.id, damage);
        });
        getZone(username, BOTH_PLAY).forEach(c -> {
            if(c.isDamageable()){
                dealDamage(sourceId, c.id, damage);
            }
        });
    }

    public void donate(UUID donatedCardId, String newController, Zone newZone) {
        Card c = extractCard(donatedCardId);
        String oldController = c.controller;
        c.controller = newController;
        getZone(newController, newZone).add(c);
        addEvent(Event.donate(oldController, newController, c));
    }

    private void chooseAttackers() {
        out.println("Updating players from chooseAttackers. AP: " + activePlayer);
        updatePlayers();
        Arraylist<AttackerAssignment> attackerIds = selectAttackers(turnPlayer);
        out.println("Attackers: "+attackerIds);
        attackerIds.forEach(c ->{
            Unit u = (Unit)getCard(UUID.fromString(c.getAttackerId()));
            u.setAttacking(UUID.fromString(c.getAttacksTo()));
            attackers.add(u.id);
        });
    }

    private void unsetAttackers() {
        attackers.forEach(u -> {((Unit)getCard(u)).unsetAttacking();});
        attackers.clear();
    }
    
    private void unsetBlockers() {
        blockers.forEach(u -> {((Unit)getCard(u)).unsetBlocking();});
        blockers.clear();
    }


    private void chooseBlockers() {
        out.println("Updating players from chooseBlockers. AP: " + activePlayer);
        updatePlayers();
        Arraylist<BlockerAssignment> assignedBlockers = selectBlockers(getOpponentName(turnPlayer));
        out.println("Blockers: "+ assignedBlockers);
        assignedBlockers.forEach(c ->{
            UUID blockerId = UUID.fromString(c.getBlockerId());
            UUID blockedBy = UUID.fromString(c.getBlockedBy());
            
            ((Unit)getCard(blockedBy)).addBlocker(blockerId);
            Unit blocker = (Unit)getCard(blockerId);
            blocker.setBlocking(blockedBy);
            blockers.add(blockerId);
        });
        
    }



    private void dealCombatDamage() {
        attackers.forEach(a -> {
            ((Unit)getCard(a)).dealAttackDamage(this);
        });
        blockers.forEach(b -> {
            ((Unit)getCard(b)).dealBlockDamage(this);
        });
    }


    public void saveGameState(String filename) {
        try {

            FileOutputStream fileOut = new FileOutputStream(filename+".gamestate");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            System.out.println("Game state is succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void assignDamage(UUID id, Arraylist<UUID> possibleTargets, int damage) {
        out.println("Updating players from assignDamage. AP: " + activePlayer);
        updatePlayers();
        Arraylist<DamageAssignment> assignedDamages = assignDamage(turnPlayer, id, possibleTargets, damage);
        out.println("Damage distribution: "+ assignedDamages);
        assignedDamages.forEach(c ->{
            UUID targetId = UUID.fromString(c.getTargetId());
            int assignedDamage = c.getDamage();
            dealDamage(id, targetId, assignedDamage);
        });
    }

    private Arraylist<DamageAssignment> assignDamage(String username, UUID id, Arraylist<UUID> possibleTargets, int damage) {
        out.println("Sending Assign Damage Message to " + username);
        AssignDamage.Builder b = AssignDamage.newBuilder()
                .setDamageSource(id.toString())
                .addAllPossibleTargets(possibleTargets.parallelStream().map(uuid -> {return uuid.toString();}).collect(Collectors.toList()))
                .setTotalDamage(damage)
                .setGame(toGameState(username));
        try {
            send(username, ServerGameMessage.newBuilder().setAssignDamage(b));
            DamageAssignment[] l = (DamageAssignment[])response.take();
            return new Arraylist<>(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }


    public enum Zone {
        DECK, HAND, PLAY, BOTH_PLAY, SCRAPYARD, VOID, STACK;
    }

}
