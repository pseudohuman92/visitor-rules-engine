package game;

import card.Card;
import card.properties.Triggering;
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

    public String name;
    public UUID id;
    public Deck deck;
    public int energy;
    public int numOfStudiesLeft;
    public ArrayList<Card> sources;
    public ArrayList<Card> hand;
    public ArrayList<Card> discardPile;
    public ArrayList<Card> voidPile;
    public ArrayList<Card> inPlayCards;
    public Hashmap<Knowledge, Integer> knowledgePool;
 
    public ArrayList<Triggering> triggeringCards;
    
    /**
     *
     * @param name
     * @param deck
     */
    public Player (String name, Deck deck){
        this.name = name;
        id = randomUUID();
        this.deck = deck;
        energy = 0;
        numOfStudiesLeft = 1;
        sources = new ArrayList<>();
        hand = new ArrayList<>();
        discardPile = new ArrayList<>();
        voidPile = new ArrayList<>();
        inPlayCards = new ArrayList<>();
        knowledgePool = new Hashmap<>();
        triggeringCards = new ArrayList<>();
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
    
    /**
     *
     * @param count
     */
    public void purge(int count) {
        voidPile.addAll(deck.draw(count));
        //TODO: Check loss
    }
    
    /**
     *
     * @param cardID
     * @return
     */
    public Card getCardFromHandByID (UUID cardID){
        for (Card card : hand) {
            if(card.id.equals(cardID)){ 
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
    public Card getCardFromPlayByID (UUID cardID){
        for (Card card : inPlayCards) {
            if(card.id.equals(cardID)){ 
                return card;
            }
        }
        return null;
    }
    
    public Card getCardByID(UUID cardID) {
        Card c = getCardFromHandByID (cardID);
        if (c != null) {
            return c;
        }
        c = getCardFromPlayByID(cardID);
        if (c != null) {
            return c;
        }
        return null;
    }
    /**
     *
     * @param cards
     */
    public void discard(ArrayList<UUID> cards){
        cards.stream().map((cardID) -> getCardFromHandByID(cardID)).map((card) -> {
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
        numOfStudiesLeft = 1;
        inPlayCards.forEach((card) -> card.depleted = false);
    }
    
    /**
     *
     * @param knowl
     */
    public void addKnowledge(Hashmap<Knowledge, Integer> knowl){
        knowl.forEach((k, i) -> {
            knowledgePool.merge(k, i, (a, b) -> a + b);
        });
    }
    
    /**
     *
     * @param cardKnowledge
     * @return
     */
    public boolean hasKnowledge(Hashmap<Knowledge, Integer> cardKnowledge){
        boolean result = true; 
        result = cardKnowledge.keySet().stream().map((k) -> knowledgePool.containsKey(k) && 
                (cardKnowledge.get(k) <= knowledgePool.get(k))).reduce(result, (accumulator, _item) -> accumulator & _item);
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
        sb.append("  ID: ").append(id).append("\n");
        sb.append("  Energy: ").append(energy).append(" / ").append(sources.size()).append("\n");
        sb.append("  Knowledge\n");
        knowledgePool.forEach((k, c) ->{
            sb.append("    ").append(k).append(": ").append(c).append("\n");
        });
        sb.append("  Source Play Count: ").append(numOfStudiesLeft).append("\n");
        sb.append("  Hand \n").append(list(hand, "    ")).append("\n");
        sb.append("  Items \n").append(list(inPlayCards, "    ")).append("\n");
        sb.append("  Discard Pile \n").append(list(discardPile, "    ")).append("\n");
        sb.append("  ").append(deck).append("\n");
        return sb.toString();
    }

    public void addTriggerEvent(Game game, Event e) {
        for (int i = 0; i < triggeringCards.size(); i++){
            triggeringCards.get(i).checkEvent(game, e);
        }
    }

}
