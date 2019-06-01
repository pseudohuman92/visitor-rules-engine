/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.server;

import com.visitor.game.*;
import com.visitor.protocol.ServerMessages.NewGame;
import com.visitor.protocol.ServerMessages.ServerMessage;
import com.visitor.protocol.ServerGameMessages.*;
import com.visitor.helpers.Hashmap;
import java.io.IOException;
import com.visitor.helpers.Arraylist;
import com.visitor.protocol.ServerMessages.LoginResponse;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;

/**
 *
 * @author pseudo
 */
public class GameServer {
    
    public Hashmap<String, GeneralEndpoint> playerConnections;
    public Hashmap<UUID, Game> games;
    public Arraylist<String> chatLog;
    public Arraylist<Player> gameQueue;

    public GameServer() {
        playerConnections = new Hashmap<>();
        chatLog = new Arraylist<>();
        games = new Hashmap<>();
        gameQueue = new Arraylist<>();
    }


    void activateCard(UUID gameID, String username, UUID cardID) {
        games.get(gameID).activateCard(username, cardID);
        games.get(gameID).updatePlayers();
    }

    void concede(UUID gameID, String username) {
        games.get(gameID).lose(username);
    }
    
    void redraw(UUID gameID, String username) {
        games.get(gameID).redraw(username);
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

    synchronized ServerGameMessage getLastMessage(UUID gameID, String username) {
        return games.get(gameID).getLastMessage(username);
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
                    .setGameId(playerGames.size()>0?playerGames.get(0).toString():"")));
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized void removeConnection(String username) {
        playerConnections.removeFrom(username);
        for(int i = 0; i < gameQueue.size(); i++){
            Player p = gameQueue.get(i);
            if(p.username.equals(username)){
                gameQueue.remove(p);
                i--;
            }
        }
    }

    synchronized void addGameConnection(UUID gameID, String username, GameEndpoint connection) {
        games.get(gameID).addConnection(username, connection);
    }
    
    synchronized void removeGameConnection(UUID gameID, String username) {
        games.get(gameID).removeConnection(username);
    }

    synchronized void joinQueue(String username, String[] decklist) {
        if (gameQueue.isEmpty()){
            System.out.println("Adding " + username + " to game queue!");
            gameQueue.add(new Player(username, decklist));
        } else {
            if(gameQueue.get(0).username.equals(username))
                    return;
            Player p1 = gameQueue.remove(0);
            System.out.println("Starting a new game with " + username + " and " + p1);
            Game g = new Game(p1, new Player(username, decklist));
            games.putIn(g.getId(), g);
            try {
                playerConnections.get(p1.username).send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGame(g.toGameState(p1.username))));
                playerConnections.get(username).send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGame(g.toGameState(username))));
            } catch (IOException | EncodeException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void addToResponseQueue(UUID gameID, Object o) {
        games.get(gameID).addToResponseQueue(o);
    }

    public void removeGame(UUID gameID) {
        games.remove(gameID);
    }
}
