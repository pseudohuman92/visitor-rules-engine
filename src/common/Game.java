/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import common.gameobjects.Phase;
import common.gameobjects.Player;
import java.util.HashMap;

/**
 *
 * @author pseudo
 */
public class Game {
    
    //(player_name, player) pairs
    private HashMap<String, Player> players;
    private String activePlayer;
    private Phase phase;
    
    public ClientGame toClientGame(String playerName, String opponentName) {
        return new ClientGame (players.get(playerName), players.get(opponentName).toOpponent(), activePlayer, phase);
    }

    /**
     * @return the players
     */
    public HashMap<String, Player> getPlayers() {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    /**
     * @return the activePlayer
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * @param activePlayer the activePlayer to set
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * @return the phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * @param phase the phase to set
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
