package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.game.Player;
import com.ccg.ancientaliens.enums.*;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.*;
import helpers.Hashmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;

/**
 *
 * @author pseudo
 */
public abstract class Card implements Serializable {

    /**
     * Ratio of the displayed cards in the height/width format.
     */
    public static final double RATIO = 3.5 / 2.5;
    
    /**
     * Extracts the uuids of the given list of cards while preserving their order.
     * @param cards
     * @return
     */
    public static ArrayList<UUID> toUUIDList(ArrayList<Card> cards) {
        ArrayList<UUID> uuids = new ArrayList<>();
        while (!cards.isEmpty()){
            Card c = cards.remove(0);
            uuids.add(c.id);
        }
        return uuids;
    }
    /**
     * Sorts given list of cards in the order of the provided uuids.
     * If a card appears in the card list but not in id list, card is ignored.
 If a id appears in id list but there is no card corresponding to it in the card list, id is ignored.
     * @param cards
     * @param uuids
     * @return
     */
    public static ArrayList<Card> sortByID(ArrayList<Card> cards, ArrayList<UUID> uuids) {
        ArrayList<Card> sorted = new ArrayList<>();
        while (!uuids.isEmpty()){
            UUID u = uuids.remove(0);
            for (Card c : cards){
                if (c.id.equals(u)){
                    sorted.add(c);
                    break;
                }
            }
        }
        return sorted;
    }

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
    
    public ArrayList<Subtype> subtypes;

    /**
     * Illustration of the card.
     */
    public String image;

    /**
     * Owner of the card. This is the player who started the game with the card in his deck.
     */
    public String owner;

    /**
     * Controller of the card. This is the player who currently have the card in play, hand, deck, void or scrapyard.
     */
    public String controller;

    /**
     * General purpose Data field for holding information like targets etc.
     */
    public ArrayList<Serializable> supplementaryData;

    /**
     * Flag to indicate if the card is depleted.
     */
    public boolean depleted;

    /**
     * Flag to indicate if the card is marked for any reason.
     */
    public boolean marked;

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
            String text, String image, String owner) {
        id = randomUUID();
        counters = new Hashmap<>();
        supplementaryData = new ArrayList<>();
        subtypes = new ArrayList<>();
        this.name = name;
        this.cost = cost;
        this.knowledge = knowledge;
        this.text = text;
        this.image = image;
        this.owner = owner;
        controller = owner;
        
        depleted = false;
        marked = false;
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
        Player player = game.players.get(controller);
        return game.activePlayer.equals(controller) 
            && player.numOfStudiesLeft > 0 
            && game.stack.isEmpty();
    }
    
    /**
     * Called by server when this card is played.
     * Default behavior is that it deducts the energy cost of the card, 
     * removes it from player's hand and then puts on the stack.
     * OVERRIDE IF: Card has an alternative cost (like X) or a special effect when played.
     * @param game
     */
    public void play(Game game) {
        Player player = game.players.get(controller);
        player.energy -= cost;
        game.addToStack(this);
    }

    /**
     * Called by the server when you choose to study this card.
     * It increases player's maximum energy and adds knowledgePool.
     * OVERRIDE IF: Card has a special effect when studied or card is multicolor.
     * @param game
     */
    public void study(Game game) {
        Player player = game.players.get(owner);
        player.voidPile.add(this);
        player.energy++;
        player.maxEnergy++;
        player.addKnowledge(getKnowledgeType());
        player.numOfStudiesLeft--;
    }

    public Hashmap<Knowledge, Integer> getKnowledgeType() {
        Hashmap<Knowledge, Integer> knowledgeType = new Hashmap<>();
        knowledge.forEach((k, i) -> { knowledgeType.put(k, 1);});
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
        //counters.merge(name, count, (a, b) -> a + b);
    }

    /**
     * Function that clears status flags and supplementary data of the card.
     * 
     */
    public void clear() {
        depleted = false;
        marked = false;
        supplementaryData = new ArrayList<>();
    }

    public Types.Card toCardMessage() {
        Types.Card.Builder b = Types.Card.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setDepleted(depleted)
                .setMarked(marked);
        //TODO: add counters
        //TODO: add targets
        return b.build();
    }
}
