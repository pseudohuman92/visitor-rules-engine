package game;

import card.Card;
import card.properties.Triggering;
import card.types.Item;
import enums.Knowledge;
import static helpers.Debug.list;
import helpers.Hashmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;

/**
 *
 * @author pseudo
 */
public class Player implements Serializable {

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
    public Deck deck;

    /**
     *
     */
    public int energy;

    /**
     *
     */
    public int playableSource;

    /**
     *
     */
    public ArrayList<Card> sources;

    /**
     *
     */
    public ArrayList<Card> hand;

    /**
     *
     */
    public ArrayList<Card> discardPile;
    
    public ArrayList<Card> voidPile;

    /**
     *
     */
    public ArrayList<Item> items;

    /**
     *
     */
    public Hashmap<Knowledge, Integer> knowledge;
 
    public ArrayList<Triggering> triggers;
    
    /**
     *
     * @param name
     * @param deck
     */
    public Player (String name, Deck deck){
        this.name = name;
        uuid = randomUUID();
        life = 30;
        this.deck = deck;
        energy = 0;
        playableSource = 1;
        sources = new ArrayList<>();
        hand = new ArrayList<>();
        discardPile = new ArrayList<>();
        voidPile = new ArrayList<>();
        items = new ArrayList<>();
        knowledge = new Hashmap<>();
        triggers = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public Opponent toOpponent ()
    {
        return new Opponent(this);
    }
    
    /**
     *
     * @param count
     */
    public void draw(int count){
        hand.addAll(deck.draw(count));
        //TODO: Check loss
    }
    
    public void purge(int count) {
        voidPile.addAll(deck.draw(count));
        //TODO: Check loss
    }
    
    /**
     *
     * @param cardID
     * @return
     */
    public Card fromHand(UUID cardID){
        for (Card card : hand) {
            if(card.uuid.equals(cardID)){ 
                return card;
            }
        }
        return null;
    }
    
    /**
     *
     * @param cardID
     * @return
     */
    public Item fromItems(UUID cardID){
        for (Item card : items) {
            if(card.uuid.equals(cardID)){ 
                return card;
            }
        }
        return null;
    }
    
    /**
     *
     * @param cards
     */
    public void discard(ArrayList<UUID> cards){
        cards.stream().map((cardID) -> fromHand(cardID)).map((card) -> {
            hand.remove(card);
            return card;
        }).forEachOrdered((card) -> {
            discardPile.add(card);
        });
    }
    
    /**
     *
     */
    public void mulligan(){
        int size = hand.size();
        if(size > 0){
            deck.deck.addAll(hand);
            hand = new ArrayList<>();
            deck.shuffle();
            draw(size -1);
        }
    }
    
    /**
     *
     */
    public void newTurn(){
        energy = sources.size();
        playableSource = 1;
        items.forEach((card) -> card.depleted = false);
    }
    
    /**
     *
     * @param knowl
     */
    public void addKnowledge(Hashmap<Knowledge, Integer> knowl){
        knowl.forEach((k, i) -> {
            knowledge.merge(k, i, (a, b) -> a + b);
        });
    }
    
    /**
     *
     * @param cardKnowledge
     * @return
     */
    public boolean hasKnowledge(Hashmap<Knowledge, Integer> cardKnowledge){
        boolean result = true; 
        result = cardKnowledge.keySet().stream().map((k) -> knowledge.containsKey(k) && (cardKnowledge.get(k) <= knowledge.get(k))).reduce(result, (accumulator, _item) -> accumulator & _item);
        return result;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("xx Player xx\n");
        sb.append("  Player Name: ").append(name).append("\n");
        sb.append("  UUID: ").append(uuid).append("\n");
        sb.append("  Life: ").append(life).append("\n");
        sb.append("  Energy: ").append(energy).append(" / ").append(sources.size()).append("\n");
        sb.append("  Knowledge\n");
        knowledge.forEach((k, c) ->{
            sb.append("    ").append(k).append(": ").append(c).append("\n");
        });
        sb.append("  Source Play Count: ").append(playableSource).append("\n");
        sb.append("  Hand \n").append(list(hand, "    ")).append("\n");
        sb.append("  Items \n").append(list(items, "    ")).append("\n");
        sb.append("  Discard Pile \n").append(list(discardPile, "    ")).append("\n");
        sb.append("  ").append(deck).append("\n");
        return sb.toString();
    }

}
