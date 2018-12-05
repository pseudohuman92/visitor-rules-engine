/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import enums.Phase;
import cards.Card;
import game.Opponent;
import game.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class ClientGame implements Serializable {
    public Player player;
    public Opponent opponent;
    public ArrayList<Card> stack;
    public String turnPlayer;
    public String activePlayer;
    public Phase phase;
    public UUID uuid;
    
    public ClientGame(Player player, Opponent opponent, ArrayList<Card> stack, String turnPlayer, 
            String activePlayer, Phase phase, UUID uuid){
        this.player = player;
        this.opponent = opponent;
        this.stack = stack;
        this.turnPlayer = turnPlayer;
        this.activePlayer = activePlayer;
        this.phase = phase;
        this.uuid = uuid;
    }
    
    
    
    public boolean canPlaySource(){
        return player.name.equals(turnPlayer) 
            && phase == Phase.MAIN
            && player.playableSource > 0;
    }
    
    public boolean hasInitiative(){
        return activePlayer.equals(player.name);
    }
}
