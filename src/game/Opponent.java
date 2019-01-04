/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import card.Card;
import card.types.Item;
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

    /**
     *
     */
    public String name;

    /**
     *
     */
    public UUID uuid;

    /**
     *
     */
    public int life;

    /**
     *
     */
    public int deckSize;

    /**
     *
     */
    public int handSize;

    /**
     *
     */
    public int energy;

    /**
     *
     */
    public ArrayList<Card> sources;

    /**
     *
     */
    public ArrayList<Card> discardPile;

    /**
     *
     */
    public ArrayList<Item> items;

    /**
     *
     */
    public Hashmap<Knowledge, Integer> knowledge;

    /**
     *
     * @param player
     */
    public Opponent (Player player) {
        this.name = player.name;
        this.uuid = player.uuid;
        this.life = player.life;
        this.energy = player.energy;
        this.deckSize = player.deck.size();
        this.handSize = player.hand.size();
        this.sources = player.sources;
        this.discardPile = player.discardPile;
        this.items = player.items;
        this.knowledge = player.knowledge;
    }
}
