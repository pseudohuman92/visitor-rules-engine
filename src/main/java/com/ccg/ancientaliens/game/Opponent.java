/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.Card;
import enums.Knowledge;
import helpers.Hashmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Opponent implements Serializable {

    public String name;
    public UUID id;
    public int deckSize;
    public int handSize;
    public int energy;

    public ArrayList<Card> sources;
    public ArrayList<Card> discardPile;
    public ArrayList<Card> cardsInPlay;
    public Hashmap<Knowledge, Integer> knowledgePool;

    /**
     *
     * @param player
     
    public Opponent (Player player) {
        this.name = player.name;
        this.id = player.id;
        this.energy = player.energy;
        this.deckSize = player.deck.size();
        this.handSize = player.hand.size();
        this.sources = player.sources;
        this.discardPile = player.discardPile;
        this.cardsInPlay = player.inPlayCards;
        this.knowledgePool = player.knowledgePool;
    }
    * */
}
