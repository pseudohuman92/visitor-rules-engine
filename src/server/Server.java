/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

//import common.Game;
import cards.Activation;
import cards.Card;
import cards.Item;
import enums.Knowledge;
import enums.Type;
import game.Deck;
import game.Game;
import enums.Phase;
import game.Player;
import game.Table;
import helpers.Debug;
import helpers.Hashmap;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import network.Connection;
import network.Message;

/**
 *
 * @author pseudo
 */
public class Server {
    public Hashmap<String, Connection> players;
    public Hashmap<UUID, Table> tables;
    public Hashmap<UUID, Game> games;
    public ArrayList<String> chatLog;
    
    public ServerSocket serverSocket;
    public ServerSocket serverGameSocket;
    public int port;
    public int gamePort;
    
    public Server() {
        players = new Hashmap<>();
        tables = new Hashmap<>();
        chatLog = new ArrayList<>();
        games = new Hashmap<>();
    }
    
    //Game Related Functions
    public synchronized void addGameConnection(UUID gameID, String username, Connection connection){
        Game game = games.get(gameID);
        game.communicationConnections.put(username, connection);
    }
    
    public synchronized void addInteractionConnection(UUID gameID, String username, Connection connection){
        Game game = games.get(gameID);
        game.interactionConnections.put(username, connection);
    }
    
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
    
    public synchronized void keep(UUID gameID, String username){
        Game game = games.get(gameID);
        game.activePlayer = game.getOpponentName(game.activePlayer);
        if (game.activePlayer.equals(game.turnPlayer)) {
            game.changePhase();
            processPhaseChange(game);
        }
    }
    
    public synchronized void concede(UUID gameID, String username){
        Game game = games.get(gameID);
        Connection connection = game.communicationConnections.get(username);
        connection.send(Message.lose());
        connection = game.communicationConnections.get(game.getOpponentName(username));
        connection.send(Message.win());
        removeTable(gameID, username);
        removeGame(gameID);
    }
    
    public synchronized void removeTable(UUID gameID, String username){
        Game game = games.get(gameID);
        for (Iterator<Table> it = tables.values().iterator(); it.hasNext();) {
            Table table = it.next();
            if ((table.creator.equals(username) && table.opponent.equals(game.getOpponentName(username)))
                ||(table.opponent.equals(username) && table.creator.equals(game.getOpponentName(username)))){
                tables.remove(table.uuid);
            }
        }
    }
    
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
    
    public synchronized void updateGame(UUID gameID){
        Game game = games.get(gameID);
        Debug.println(game.toString());
        game.communicationConnections.forEach((name, connection) -> {
            connection.send(Message.updateGame(game.toClientGame(name)));
        });
    }

    public synchronized void play(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets) {
        Game game = games.get(gameID);
        Player player = game.players.get(username);
        Card card = player.fromHand(cardID);
        card.supplimentaryData = targets;
        card.play(game);
        game.activePlayer = game.getOpponentName(player.name);
    }
    
    public synchronized void activate(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets) {
        Game game = games.get(gameID);
        Player player = game.players.get(username);
        Item card = player.fromItems(cardID);
        card.supplimentaryData = targets;
        card.activate(game);
        game.activePlayer = game.getOpponentName(player.name);
    }
    
    public synchronized void playSource(UUID gameID, String username, UUID cardID, Hashmap<Knowledge, Integer> knowledge) {
        Game game = games.get(gameID);
        Player player = game.players.get(username);
        Card card = player.fromHand(cardID);
        card.playAsSource(game, knowledge);
    }

    public synchronized void processPhaseChange(Game game){
        switch (game.phase){
            case BEGIN:
                //TODO: refactor and generalize this
                
                /*
                    ArrayList<Card> playerStartTriggers = game.getPlayerStartTriggers();
                    if(playerStartTriggers.size() == 1){
                        game.addToStack(playerStartTriggers.get(0));
                    } else if (playerStartTriggers.size() > 1){
                        game.addToStack(game.orderCards(game.turnPlayer, playerStartTriggers));
                    }
                    if(!game.stack.isEmpty()){
                        updateGame(game.uuid);
                        resolveStack(game);
                    }
                    game.changePhase(true);
                    processPhaseChange(game);
                */
            break;
            case MAIN:
                //TODO: process main triggers
                break;
            case END:
                //TODO: process end triggers
                //TODO: check hand size
                game.changePhase();
                processPhaseChange(game);
            break;
        }
    }
    
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
    
    public synchronized void newGame(Table table) {
        Game game = new Game(table);
        games.put(game.uuid, game);
        Connection creatorConnection = players.get(table.creator);
        Connection opponentConnection = players.get(table.opponent);
        creatorConnection.send(Message.newGame(game.toClientGame(table.creator)));
        opponentConnection.send(Message.newGame(game.toClientGame(table.opponent)));
    }
    
    //Char Related Functions
    public synchronized void appendToChatLog(String message) {
        chatLog.add(message);
        if (chatLog.size() > 1000) {
            chatLog.remove(0);
        }
    }
    
    public synchronized void updateChatLogs() {
        players.values().forEach((entry) -> {
            entry.send(Message.updateChatLog(chatLog));
        });
    }
    
    public synchronized void updatePlayers() {
        ArrayList<String> names = new ArrayList<>();
        players.forEach((name, entry) -> names.add(name));
        players.values().forEach((entry) -> {
            entry.send(Message.updatePlayers(names));
        });
    }
    
    //Table Related Functions
    public synchronized void createTable(String creator, Deck creatorDeck){
        UUID uuid =UUID.randomUUID();
        tables.put(uuid, new Table(creator, creatorDeck, uuid));
    }
    
    public synchronized void joinTable(String player, Deck deck, UUID uuid){
        Table table = tables.get(uuid);
        Connection playerConnection = players.get(player);
        if (table != null && table.opponent== null && !table.creator.equals(player)) {
            table.opponent = player;
            table.opponentDeck = deck;
            newGame(table);            
        } else {
            playerConnection.send(Message.fail("Wrong UUID or table is full"));
        } 
    }
    
    public synchronized void updateTables() {
        players.values().forEach((entry) -> {
            entry.send(Message.updateTables(tables));
        });
    }

    //Login Related Functions
    public synchronized boolean isLoggedIn(String username) {
        return players.containsKey(username); 
    }
    
    public synchronized void addLogin(String username, Connection connection) {
        players.put(username, connection);
        System.out.println(username + " is logged in successfully.");
    }
    
    public synchronized void removeLogin(String username) {
        if(isLoggedIn(username)){
            players.remove(username);
            System.out.println(username + " is logged out successfully.");
        }
        for (Iterator<Table> it = tables.values().iterator(); it.hasNext();) {
            Table table = it.next();
            if (table.creator.equals(username) || table.opponent.equals(username)){
                tables.remove(table.uuid);
            }
        }
        for (Iterator<Game> it = games.values().iterator(); it.hasNext();) {
            Game game = it.next();
            if (game.players.get(username) != null){
                Connection connection = game.communicationConnections.get(game.getOpponentName(username));
                connection.send(Message.win());
                removeGame(game.uuid);
            }
        }
    }
}
