/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import common.gameobjects.Opponent;
import common.gameobjects.Phase;
import common.gameobjects.Player;
import java.io.Serializable;

/**
 *
 * @author pseudo
 */
public class ClientGame implements Serializable {
    private Player player;
    private Opponent opponent;
    private String activePlayer;
    private Phase phase;
    
    public ClientGame(Player player, Opponent opponent, String activePlayer, Phase phase){
        this.player = player;
        this.opponent = opponent;
        this.activePlayer = activePlayer;
        this.phase = phase;
    }
}
