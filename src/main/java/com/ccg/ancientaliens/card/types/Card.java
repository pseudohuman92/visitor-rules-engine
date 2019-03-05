package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.game.Player;
import com.ccg.ancientaliens.helpers.Arraylist;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.*;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.io.Serializable;
import java.util.UUID;
import static java.util.UUID.randomUUID;

/**
 *
 * @author pseudo
 */
public abstract class Card implements Serializable {

    // intrinsic variables

    /**
     * Unique identifier for the card.
     */
    public UUID id;

    /**
     * Name of the card.
     */
    public String name;

    /**
     * Energy cost of the card.
     */
    public int cost;

    /**
     * Knowledge requirement of the card.
     */
    public Hashmap<Knowledge, Integer> knowledge;

    /**
     * Text of the card
     */
    public String text;
    
    public Arraylist<String> subtypes;

    /**
     * Owner of the card. This is the player who started the game with the card in his deck.
     */
    public String owner;

    /**
     * Controller of the card. This is the player who currently have the card in play, hand, deck, void or scrapyard.
     */
    public String controller;

    /**
     * Flag to indicate if the card is depleted.
     */
    public boolean depleted;

    /**
     * Collection of counters on the card.
     */
    public Hashmap<Counter, Integer> counters;

    /**
     * This is the default constructor for creating a card.
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Card(String name, int cost, Hashmap<Knowledge, Integer> knowledge,
            String text, String owner) {
        id = randomUUID();
        counters = new Hashmap<>();
        subtypes = new Arraylist<>();
        this.name = name;
        this.cost = cost;
        this.knowledge = knowledge;
        this.text = text;
        this.owner = owner;
        this.controller = owner;
        this.depleted = false;
    }
    
    /**
     * Called by client to check if you can play this card in current game state.
     * @param game
     * @return
     */
    public abstract boolean canPlay(Game game);
    
    /**
     * Called by client to check if you can study this card in current game state.
     * Default implementation just checks the game if the controller can study.
     * OVERRIDE IF: Card has special conditions to be studied.
     * @param game
     * @return
     */
    public boolean canStudy (Game game){
       return game.canStudy(controller);
    }
    
    /**
     * Called by server when this card is played.
     * Default behavior is that it deducts the energy cost of the card, 
     * removes it from player's hand and then puts on the stack.
     * OVERRIDE IF: Card has an alternative cost (like X) or a special effect when played.
     * @param game
     */
    public void play(Game game) {
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }

    /**
     * Called by the server when you choose to study this card.
     * It increases player's maximum energy and adds knowledgePool.
     * OVERRIDE IF: Card has a special effect when studied or card is multicolor.
     * @param game
     */
    public void study(Game game) {
        Player player = game.getPlayer(controller);
        player.voidPile.add(this);
        player.energy++;
        player.maxEnergy++;
        player.addKnowledge(getKnowledgeType());
        player.numOfStudiesLeft--;
    }

    public Hashmap<Knowledge, Integer> getKnowledgeType() {
        Hashmap<Knowledge, Integer> knowledgeType = new Hashmap<>();
        knowledge.forEach((k, i) -> { knowledgeType.putIn(k, 1);});
        return knowledgeType;
    }
    /**
     * This is the function that describes what is the effect of the card when it is resolved.
     * This function contains the business logic of the card effect.
     * @param game
     */
    public abstract void resolve(Game game);
    
    /**
     * Function that adds counters to the card.
     * @param name
     * @param count
     */
    public void addCounters(Counter name, int count) {
        counters.merge(name, count, (a, b) -> a + b);
    }
    
    public void removeCounters(Counter name, int count) {
        int k = counters.get(name);
        if (k <= count){
            counters.remove(name);
        } else {
            counters.put(name, k - count);
        }
    }

    /**
     * Function that clears status flags and supplementary data of the card.
     * 
     */
    public void clear() {
        depleted = false;
        counters = new Hashmap<>();
    }
    
    public void copyPropertiesFrom(Card c) {
        id = c.id;
        owner = c.owner;
        controller = c.controller;
        counters = c.counters;
        depleted = c.depleted;
    }

    public Types.Card.Builder toCardMessage() {
        Types.Card.Builder b = Types.Card.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setDepleted(depleted)
                .setDescription(text)
                .setCost(Integer.toString(cost))
                .setType("Card")
                .addAllSubtypes(subtypes);
        counters.forEach((k, i) -> {
            b.addCounters(CounterGroup.newBuilder()
                    .setCounter(k)
                    .setCount(i).build());
        });
        knowledge.forEach((k, i) -> {
            b.addKnowledgeCost(KnowledgeGroup.newBuilder()
                    .setKnowledge(k)
                    .setCount(i).build());
        });
        //TODO: add targets
        return b;
    }
    
}
