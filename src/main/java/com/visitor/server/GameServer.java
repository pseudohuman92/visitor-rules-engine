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


    void activateCard(UUID gameID, String userId, UUID cardID) {
        games.get(gameID).activateCard(userId, cardID);
        games.get(gameID).updatePlayers();
    }

    void concede(UUID gameID, String userId) {
        games.get(gameID).lose(userId);
    }
    
    void mulligan(UUID gameID, String userId) {
        games.get(gameID).redraw(userId);
        games.get(gameID).updatePlayers();
    }
    
    void keep(UUID gameID, String userId) {
        games.get(gameID).keep(userId);
        games.get(gameID).updatePlayers();
    }
    
    void pass(UUID gameID, String userId) {
        games.get(gameID).pass(userId);
        games.get(gameID).updatePlayers();
    }
    
    void studyCard(UUID gameID, String userId, UUID cardID) {
        games.get(gameID).studyCard(userId, cardID);
        games.get(gameID).updatePlayers();
    }
    
    void playCard(UUID gameID, String userId, UUID cardID) {
        games.get(gameID).playCard(userId, cardID);
        games.get(gameID).updatePlayers();
    }

    synchronized ServerGameMessage getLastMessage(UUID gameID, String userId) {
        return games.get(gameID).getLastMessage(userId);
    }

    synchronized void addConnection(String userId, GeneralEndpoint connection) {
        try {
            playerConnections.putIn(userId, connection);
            Arraylist<UUID> playerGames = new Arraylist<>();
            games.forEach((id, game) -> {
                if(game.isAPlayer(userId)){
                    playerGames.add(id);
                }
            });
            connection.send(ServerMessage.newBuilder().setLoginResponse(LoginResponse.newBuilder()
                    .setGameId(playerGames.size()>0?playerGames.get(0).toString():"")));
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    synchronized void removeConnection(String userId) {
        playerConnections.removeFrom(userId);
        for(int i = 0; i < gameQueue.size(); i++){
            Player p = gameQueue.get(i);
            if(p.userId.equals(userId)){
                gameQueue.remove(p);
                i--;
            }
        }
    }

    synchronized void addGameConnection(UUID gameID, String userId, GameEndpoint connection) {
        games.get(gameID).addConnection(userId, connection);
    }
    
    synchronized void removeGameConnection(UUID gameID, String userId) {
        games.get(gameID).removeConnection(userId);
    }

    synchronized void joinQueue(String userId, String[] decklist) {
        if (gameQueue.isEmpty()){
            System.out.println("Adding " + userId + " to game queue!");
            gameQueue.add(new Player(userId, decklist));
        } else {
            if(gameQueue.get(0).userId.equals(userId))
                    return;
            Player p1 = gameQueue.remove(0);
            System.out.println("Starting a new game with " + userId + " and " + p1);
            Game g = new Game(p1, new Player(userId, decklist));
            games.putIn(g.getId(), g);
            try {
                playerConnections.get(p1.userId).send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGame(g.toGameState(p1.userId))));
                playerConnections.get(userId).send(ServerMessage.newBuilder()
                        .setNewGame(NewGame.newBuilder()
                                .setGame(g.toGameState(userId))));
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
