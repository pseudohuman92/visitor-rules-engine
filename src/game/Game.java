
package game;

import card.Card;
import static card.Card.sortByUUID;
import card.types.Item;
import enums.Phase;
import static enums.Phase.BEGIN;
import static enums.Phase.END;
import static enums.Phase.MAIN;
import static enums.Phase.MULLIGAN;
import helpers.Hashmap;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import network.Connection;
import network.Message;
import static network.Message.lose;
import static network.Message.order;

/**
 *
 * @author pseudo
 */
public class Game {
    
    //(player_name, player) pairs
    public Hashmap<String, Player> players;
    public Hashmap<String, Connection> communicationConnections;
    public Hashmap<String, Connection> interactionConnections;
    public String turnPlayer;
    public String activePlayer;
    public Phase phase;
    public int turnCount;
    public int passCount;
    public UUID uuid;
    
    /**
     *
     * @param table
     */
    public Game (Table table) {
        uuid = randomUUID();
        players = new Hashmap<>();
        communicationConnections = new Hashmap<>();
        interactionConnections = new Hashmap<>();
        
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
    
    /**
     *
     */
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
        phase = BEGIN;
        if(turnCount > 0){
            turnPlayer = getOpponentName(turnPlayer);
            players.get(turnPlayer).draw(1);
        }
        activePlayer = "";
        passCount = 0;
        players.get(turnPlayer).draw(1);
        players.get(turnPlayer).newTurn();
        turnCount++;
    }
    
    /**
     *
     * @param playerName
     * @return
     */
    public String getOpponentName(String playerName){
        for(String name : players.keySet()){
            if(!name.equals(playerName)){
                return name;
            }
        }
        return null;
    }
    
    /**
     *
     * @param playerName
     * @return
     */
    public ClientGame toClientGame(String playerName) {
        Player opponent = players.get(getOpponentName(playerName));
        return new ClientGame (players.get(playerName), opponent.toOpponent(), 
                turnPlayer, activePlayer, phase, uuid);
    }
    
    /**
     *
     * @param username
     * @param count
     */
    public void discard(String username, int count){
        Connection connection = interactionConnections.get(username);
        connection.send(Message.discard(toClientGame(username), count));
        Message message = connection.receive();
        if (message != null) {
            Player player = players.get(username);
            player.discard((ArrayList<UUID>)message.object);
        }
    }
    
    /**
     *
     * @param username
     * @param count
     */
    public void draw(String username, int count){
        Player player = players.get(username);
        player.draw(count);
        //TODO: Check loss
    }
    
    /**
     *
     * @param username
     * @param count
     */
    public void purge(String username, int count){
        Player player = players.get(username);
        player.purge(count);
        //TODO: Check loss
    }
    
    /**
     *
     * @param uuid
     */
    public void destroy(UUID uuid){
        Item item = getItemByID(uuid);
        players.get(item.owner).items.remove(item);
        players.get(item.owner).discardPile.add(item);
    }
    
    /**
     *
     * @param uuid
     */
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
    
    /**
     *
     * @param uuid
     */
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
    
    /**
     *
     * @param playerID
     * @return
     */
    public Player getPlayerByID(UUID playerID){
        for (Player player : players.values()) {
            if(player.uuid.equals(playerID)){ 
                return player;
            }
        }
        return null;
    }
    
    /**
     *
     * @param playerID
     * @return
     */
    public Player getPlayerByName(String playerName){
        return players.get(playerName);
    }
    
    /**
     *
     * @param itemID
     * @return
     */
    public Item getItemByID(UUID itemID){
        for (Player player : players.values()) {
            for (Item item : player.items){
                if(item.uuid.equals(itemID)){ 
                    return item;
                }
            }
        }
        return null;
    }
    
    /**
     *
     * @param itemID
     */
    public void switchOwner(UUID itemID){
        Item item = getItemByID(itemID);
        players.get(item.owner).items.remove(item);
        players.get(getOpponentName(item.owner)).items.add(item);
        item.owner = getOpponentName(item.owner);
    }

    /**
     *
     * @param player
     */
    public void win(String player) {
        Connection connection = interactionConnections.get(player);
        connection.send(Message.win());
        connection.closeConnection();
        connection = interactionConnections.get(getOpponentName(player));
        connection.send(lose());
        connection.closeConnection();
    }

    /**
     *
     * @param username
     * @param cards
     * @return
     */
    public ArrayList<Card> orderCards(String username, ArrayList<Card> cards) {
        String activePlayerStore = activePlayer;
        activePlayer = "";
        Connection connection = interactionConnections.get(username);
        connection.send(order(cards));
        Message message = connection.receive();
        if (message != null) {
            cards = sortByUUID(cards, (ArrayList<UUID>)message.object);
            activePlayer = activePlayerStore;
            return cards;
        }
        activePlayer = activePlayerStore;
        return null;
    }
    
    public void loot(String username, int x) {
        draw(username, x);
        discard(username, x);
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("***** GAME *****\n");
        sb.append("Game UUID: ").append(uuid).append("\n");
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

    /**
     *
     * @param itemID
     * @param newController
     */
    public void changeItemController(UUID itemID, String newController) {
        Item c = getItemByID(itemID);
        players.get(c.controller).items.remove(c);
        players.get(newController).items.add(c);
        c.controller = newController;
    }
}
