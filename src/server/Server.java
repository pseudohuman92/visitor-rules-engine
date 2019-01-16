/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import card.Card;
import card.types.Item;
import enums.Knowledge;
import game.Deck;
import game.Game;
import game.Player;
import game.Table;
import static helpers.Debug.println;
import helpers.Hashmap;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import network.Connection;
import network.Message;
import static network.Message.fail;
import static network.Message.lose;
import static network.Message.updateChatLog;
import static network.Message.win;

/**
 *
 * @author pseudo
 */
public class Server {

    /**
     *
     */
    public Hashmap<String, Connection> players;

    /**
     *
     */
    public Hashmap<UUID, Table> tables;

    /**
     *
     */
    public Hashmap<UUID, Game> games;

    /**
     *
     */
    public ArrayList<String> chatLog;
    
    /**
     *
     */
    public ServerSocket serverSocket;

    /**
     *
     */
    public ServerSocket serverGameSocket;

    /**
     *
     */
    public int port;

    /**
     *
     */
    public int gamePort;
    
    /**
     *
     */
    public Server() {
        players = new Hashmap<>();
        tables = new Hashmap<>();
        chatLog = new ArrayList<>();
        games = new Hashmap<>();
    }
    
    //Game Related Functions

    /**
     *
     * @param gameID
     * @param username
     * @param connection
     */
    public synchronized void addGameConnection(UUID gameID, String username, Connection connection){
        Game game = games.get(gameID);
        game.communicationConnections.put(username, connection);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @param connection
     */
    public synchronized void addInteractionConnection(UUID gameID, String username, Connection connection){
        Game game = games.get(gameID);
        game.interactionConnections.put(username, connection);
    }
    
    /**
     *
     * @param gameID
     * @param username
     */
    public synchronized void mulligan(UUID gameID, String username){
        Game game = games.get(gameID);
        Player player = game.players.get(username);
        player.mulligan();
        if (player.hand.isEmpty()){
            game.activePlayer = game.getOpponentName(game.activePlayer);
            if (game.activePlayer.equals(game.turnPlayer)) {
                game.changePhase();
                processPhaseChange(game);
            }
        }
    }
    
    /**
     *
     * @param gameID
     * @param username
     */
    public synchronized void keep(UUID gameID, String username){
        Game game = games.get(gameID);
        game.activePlayer = game.getOpponentName(game.activePlayer);
        if (game.activePlayer.equals(game.turnPlayer)) {
            game.changePhase();
            processPhaseChange(game);
        }
    }
    
    /**
     *
     * @param gameID
     * @param username
     */
    public synchronized void concede(UUID gameID, String username){
        Game game = games.get(gameID);
        Connection connection = game.communicationConnections.get(username);
        connection.send(lose());
        connection = game.communicationConnections.get(game.getOpponentName(username));
        connection.send(win());
        removeTable(gameID, username);
        removeGame(gameID);
    }
    
    /**
     *
     * @param gameID
     * @param username
     */
    public synchronized void removeTable(UUID gameID, String username){
        Game game = games.get(gameID);
        for (Table table : tables.values()) {
            if ((table.creator.equals(username) && table.opponent.equals(game.getOpponentName(username)))
                    ||(table.opponent.equals(username) && table.creator.equals(game.getOpponentName(username)))){
                tables.remove(table.uuid);
            }
        }
    }
    
    /**
     *
     * @param gameID
     */
    public synchronized void removeGame(UUID gameID){
        Game game = games.get(gameID);
        game.communicationConnections.forEach((name,conn) -> {
            conn.closeConnection();
        });
        game.interactionConnections.forEach((name,conn) -> {
            conn.closeConnection();
        });
        games.remove(gameID);
    }
    
    /**
     *
     * @param gameID
     */
    public synchronized void updateGame(UUID gameID){
        Game game = games.get(gameID);
        println(game.toString());
        game.communicationConnections.forEach((name, connection) -> {
            connection.send(Message.updateGame(game.toClientGame(name)));
        });
    }

    /**
     *
     * @param gameID
     * @param username
     * @param cardID
     * @param targets
     */
    public synchronized void play(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets) {
        Game game = games.get(gameID);
        Player player = game.players.get(username);
        Card card = player.getCardFromHandByID(cardID);
        card.supplementaryData = targets;
        card.play(game);
        game.activePlayer = game.getOpponentName(player.name);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @param cardID
     * @param targets
     */
    public synchronized void activate(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets) {
        Game game = games.get(gameID);
        Player player = game.players.get(username);
        Item card = (Item)player.getCardFromPlayByID(cardID);
        card.supplementaryData = targets;
        card.activate(game);
        game.activePlayer = game.getOpponentName(player.name);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @param cardID
     * @param knowledge
     */
    public synchronized void study(UUID gameID, String username, UUID cardID, Hashmap<Knowledge, Integer> knowledge) {
        Game game = games.get(gameID);
        Player player = game.players.get(username);
        Card card = player.getCardFromHandByID(cardID);
        card.study(game, knowledge);
    }

    /**
     *
     * @param game
     */
    public synchronized void processPhaseChange(Game game){
        switch (game.phase){
            case BEGIN:
                //TODO: process begin triggeringCards
            break;
            case MAIN:
                //TODO: process main triggeringCards
                break;
            case END:
                //TODO: process end triggeringCards
                //TODO: check hand size
                game.changePhase();
                processPhaseChange(game);
            break;
        }
    }
    
    /**
     *
     * @param gameID
     */
    public synchronized void skipInitiative(UUID gameID) {
        Game game = games.get(gameID);
        game.passCount++;
        game.activePlayer = game.getOpponentName(game.activePlayer);
        if (game.passCount == 2) {
            game.passCount = 0;
                game.changePhase();
                updateGame(game.uuid);
                processPhaseChange(game);
        }
        updateGame(game.uuid);
    }
    
    /**
     *
     * @param table
     */
    public synchronized void newGame(Table table) {
        Game game = new Game(table);
        games.put(game.uuid, game);
        Connection creatorConnection = players.get(table.creator);
        Connection opponentConnection = players.get(table.opponent);
        creatorConnection.send(Message.newGame(game.toClientGame(table.creator)));
        opponentConnection.send(Message.newGame(game.toClientGame(table.opponent)));
    }
    
    //Char Related Functions

    /**
     *
     * @param message
     */
    public synchronized void appendToChatLog(String message) {
        chatLog.add(message);
        if (chatLog.size() > 1_000) {
            chatLog.remove(0);
        }
    }
    
    /**
     *
     */
    public synchronized void updateChatLogs() {
        players.values().forEach((entry) -> {
            entry.send(updateChatLog(chatLog));
        });
    }
    
    /**
     *
     */
    public synchronized void updatePlayers() {
        ArrayList<String> names = new ArrayList<>();
        players.forEach((name, entry) -> names.add(name));
        players.values().forEach((entry) -> {
            entry.send(Message.updatePlayers(names));
        });
    }
    
    //Table Related Functions

    /**
     *
     * @param creator
     * @param creatorDeck
     */
    public synchronized void createTable(String creator, Deck creatorDeck){
        UUID uuid =randomUUID();
        tables.put(uuid, new Table(creator, creatorDeck, uuid));
    }
    
    /**
     *
     * @param player
     * @param deck
     * @param uuid
     */
    public synchronized void joinTable(String player, Deck deck, UUID uuid){
        Table table = tables.get(uuid);
        Connection playerConnection = players.get(player);
        if (table != null && table.opponent== null && !table.creator.equals(player)) {
            table.opponent = player;
            table.opponentDeck = deck;
            newGame(table);            
        } else {
            playerConnection.send(fail("Wrong UUID or table is full"));
        } 
    }
    
    /**
     *
     */
    public synchronized void updateTables() {
        players.values().forEach((entry) -> {
            entry.send(Message.updateTables(tables));
        });
    }

    //Login Related Functions

    /**
     *
     * @param username
     * @return
     */
    public synchronized boolean isLoggedIn(String username) {
        return players.containsKey(username); 
    }
    
    /**
     *
     * @param username
     * @param connection
     */
    public synchronized void addLogin(String username, Connection connection) {
        players.put(username, connection);
    }
    
    /**
     *
     * @param username
     */
    public synchronized void removeLogin(String username) {
        if(isLoggedIn(username)){
            players.remove(username);
        }
        for (Table table : tables.values()) {
            if (table.creator.equals(username) || table.opponent.equals(username)){
                tables.remove(table.uuid);
            }
        }
        for (Game game : games.values()) {
            if (game.players.get(username) != null) {
                Connection connection = game.communicationConnections.get(game.getOpponentName(username));
                connection.send(win());
                removeGame(game.uuid);
            }
        }
    }
}
