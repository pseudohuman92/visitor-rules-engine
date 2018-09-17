/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.Deck;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Table implements Serializable {
    public String creator;
    public String opponent;
    public Deck creatorDeck;
    public Deck opponentDeck;
    public UUID uuid;
    
    public Table(String creator, Deck creatorDeck, UUID uuid){
        this.creator = creator;
        this.creatorDeck = creatorDeck;
        this.uuid = uuid;
    }
    
    public String[] toStringArray(){
        String[] ret = new String[2];
        ret[0] = creator;
        ret[1] = opponent;
        return ret;
    }
    
    @Override
    public String toString(){
        return "Creator: " + creator + "\t Opponent: " + (opponent==null?"":opponent);
    }
}
