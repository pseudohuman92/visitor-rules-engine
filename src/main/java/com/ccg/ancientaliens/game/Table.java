/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.game;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Table implements Serializable {

    /**
     *
     */
    public String creator;

    /**
     *
     */
    public String opponent;

    /**
     *
     */
    public Deck creatorDeck;

    /**
     *
     */
    public Deck opponentDeck;

    /**
     *
     */
    public UUID uuid;
    
    /**
     *
     * @param creator
     * @param creatorDeck
     * @param uuid
     */
    public Table(String creator, Deck creatorDeck, UUID uuid){
        this.creator = creator;
        this.creatorDeck = creatorDeck;
        this.uuid = uuid;
    }
    
    /**
     *
     * @return
     */
    public String[] toStringArray(){
        String[] ret = new String[2];
        ret[0] = creator;
        ret[1] = opponent;
        return ret;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "Creator: " + creator + "\t Opponent: " + (opponent==null?"":opponent);
    }
}
