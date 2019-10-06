
package com.visitor.server;

import com.visitor.game.*;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.ServerMessages.LoginResponse;
import com.visitor.protocol.ServerMessages.NewGame;
import com.visitor.protocol.ServerMessages.ServerMessage;
import java.io.IOException;
import static java.lang.System.out;
import java.util.UUID;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
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
        System.out.println("Start Activate Card");
        games.get(gameID).activateCard(username, cardID);
        out.println("Updating players from activateCard.");
        games.get(gameID).updatePlayers();
        System.out.println("End Activate Card");
    }

    void concede(UUID gameID, String username) {
        games.get(gameID).gameEnd(username, false);
    }
    
    void redraw(UUID gameID, String username) {
        System.out.println("Start Redraw");
        games.get(gameID).redraw(username);
        out.println("Updating players from redraw.");
        games.get(gameID).updatePlayers();
        System.out.println("End Redraw");
    }
    
    void keep(UUID gameID, String username) {
        System.out.println("Start Keep");
        games.get(gameID).keep(username);
        out.println("Updating players from keep.");
        games.get(gameID).updatePlayers();
        System.out.println("End Keep");
    }
    
    void pass(UUID gameID, String username) {
        System.out.println("Start Pass");
        games.get(gameID).pass(username);
        out.println("Updating players from pass.");
        games.get(gameID).updatePlayers();
        System.out.println("End Pass");
    }
    
    void studyCard(UUID gameID, String username, UUID cardID) {
        System.out.println("Start Study Card");
        games.get(gameID).studyCard(username, cardID);
        out.println("Updating players from studyCard.");
        games.get(gameID).updatePlayers();
        System.out.println("End Study Card");
    }
    
    void playCard(UUID gameID, String username, UUID cardID) {
        System.out.println("Start Play Card");
        games.get(gameID).playCard(username, cardID);
        out.println("Updating players from playCard.");
        games.get(gameID).updatePlayers();
        System.out.println("End Play Card");
    }

    synchronized ServerGameMessage getLastMessage(UUID gameID, String username) {
        return games.get(gameID).getLastMessage(username);
    }

    synchronized void addConnection(String username, GeneralEndpoint connection) {
        try {
            playerConnections.putIn(username, connection);
            Arraylist<UUID> playerGames = new Arraylist<>();
            games.forEach((id, game) -> {
                if(game.isInGame(username)){
                    playerGames.add(id);
                }
            });
            connection.send(ServerMessage.newBuilder().setLoginResponse(LoginResponse.newBuilder()
                    .setGameId(playerGames.size()>0?playerGames.get(0).toString():"")));
        } catch (IOException | EncodeException ex) {
            getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
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
            out.println("Adding " + username + " to game queue!");
            gameQueue.add(new Player(username, decklist));
        } else {
            if(gameQueue.get(0).username.equals(username)) {
                return;
            }
            Player p1 = gameQueue.remove(0);
            out.println("Starting a new game with " + username + " and " + p1);
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
                getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
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
