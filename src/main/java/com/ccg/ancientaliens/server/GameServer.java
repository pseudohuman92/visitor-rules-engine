/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.server;

import com.ccg.ancientaliens.game.*;
import com.ccg.ancientaliens.protocol.Types;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class GameServer {
    
    public Hashmap<String, GeneralEndpoint> playerConnections;
    public Hashmap<UUID, Table> tables;
    public Hashmap<UUID, Game> games;
    public ArrayList<String> chatLog;
    

    public GameServer() {
        playerConnections = new Hashmap<>();
        tables = new Hashmap<>();
        chatLog = new ArrayList<>();
        games = new Hashmap<>();
    }


    void activateCard(UUID gameID, String username, UUID fromString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    void pass(UUID gameID, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void mulligan(UUID gameID, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void keep(UUID gameID, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void concede(UUID gameID, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void studyCard(UUID gameID, String username, UUID cardID, List<Types.Knowledge> knowledgeList) {
        games.get(gameID).studyCard(username, cardID);
    }
    
    void playCard(UUID gameID, String username, UUID cardID) {
        games.get(gameID).playCard(username, cardID);
    }

    void addConnection(String username, GeneralEndpoint connection) {
        playerConnections.put(username, connection);
    }

    void removeConnection(String username) {
        playerConnections.remove(username);
    }

    void addGameConnection(UUID gameID, String username, GameEndpoint connection) {
        games.get(gameID).addConnection(username, connection);
    }
    
    void removeGameConnection(UUID gameID, String username) {
        games.get(gameID).removeConnection(username);
    }
}
