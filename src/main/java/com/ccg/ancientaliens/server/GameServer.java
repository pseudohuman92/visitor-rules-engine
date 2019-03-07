/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.server;

import com.ccg.ancientaliens.game.*;
import com.ccg.ancientaliens.protocol.ServerMessages.NewGame;
import com.ccg.ancientaliens.protocol.ServerMessages.ServerMessage;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.io.IOException;
import com.ccg.ancientaliens.helpers.Arraylist;
import com.ccg.ancientaliens.protocol.ServerMessages.LoginResponse;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.websocket.EncodeException;

/**
 *
 * @author pseudo
 */
public class GameServer {
    
    public Hashmap<String, GeneralEndpoint> playerConnections;
    public Hashmap<UUID, Table> tables;
    public Hashmap<UUID, Game> games;
    public Arraylist<String> chatLog;
    public Arraylist<String> gameQueue;

    public GameServer() {
        playerConnections = new Hashmap<>();
        tables = new Hashmap<>();
        chatLog = new Arraylist<>();
        games = new Hashmap<>();
        gameQueue = new Arraylist<>();
    }


    void activateCard(UUID gameID, String username, UUID cardID) {
        games.get(gameID).activateCard(username, cardID);
        games.get(gameID).updatePlayers();
    }

    void concede(UUID gameID, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void mulligan(UUID gameID, String username) {
        games.get(gameID).mulligan(username);
        games.get(gameID).updatePlayers();
    }
    
    void keep(UUID gameID, String username) {
        games.get(gameID).keep(username);
        games.get(gameID).updatePlayers();
    }
    
    void pass(UUID gameID, String username) {
        games.get(gameID).pass(username);
        games.get(gameID).updatePlayers();
    }
    
    void studyCard(UUID gameID, String username, UUID cardID) {
        games.get(gameID).studyCard(username, cardID);
        games.get(gameID).updatePlayers();
    }
    
    void playCard(UUID gameID, String username, UUID cardID) {
        games.get(gameID).playCard(username, cardID);
        games.get(gameID).updatePlayers();
    }

    synchronized void addConnection(String username, GeneralEndpoint connection) {
        try {
            playerConnections.putIn(username, connection);
            Arraylist<UUID> playerGames = new Arraylist<>();
            games.forEach((id, game) -> {
                if(game.isAPlayer(username)){
                    playerGames.add(id);
                }
            });
            connection.send(ServerMessage.newBuilder().setLoginResponse(LoginResponse.newBuilder()
                    .addAllGameIdList(playerGames.parallelStream()
                        .map(u->{return u.toString();}).collect(Collectors.toList()))));
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized void removeConnection(String username) {
        playerConnections.removeFrom(username);
        gameQueue.remove(username);
    }

    synchronized void addGameConnection(UUID gameID, String username, GameEndpoint connection) {
        games.get(gameID).addConnection(username, connection);
    }
    
    synchronized void removeGameConnection(UUID gameID, String username) {
        games.get(gameID).removeConnection(username);
    }

    synchronized void joinTable(String username) {
        if (gameQueue.isEmpty()){
            System.out.println("Adding " + username + " to game queue!");
            gameQueue.add(username);
        } else {
            if(gameQueue.get(0).equals(username))
                    return;
            String p1 = gameQueue.remove(0);
            System.out.println("Starting a new game with " + username + " and " + p1);
            Game g = new Game(p1, username);
            games.putIn(g.getId(), g);
            try {
                playerConnections.get(p1).send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGame(g.toGameState(p1))));
                playerConnections.get(username).send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGame(g.toGameState(username))));
            } catch (IOException | EncodeException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
