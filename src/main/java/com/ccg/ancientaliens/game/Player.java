package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.properties.Triggering;
import com.ccg.ancientaliens.enums.Knowledge;
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
    public int energy;
    public int maxEnergy;
    public int numOfStudiesLeft;
    public Deck deck;
    public ArrayList<Card> hand;
    public ArrayList<Card> scrapyard;
    public ArrayList<Card> voidPile;
    public ArrayList<Card> playArea;
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
        maxEnergy = 0;
        numOfStudiesLeft = 1;
        hand = new ArrayList<>();
        scrapyard = new ArrayList<>();
        voidPile = new ArrayList<>();
        playArea = new ArrayList<>();
        knowledgePool = new Hashmap<>();
        triggeringCards = new ArrayList<>();
    }

    public void draw(int count){
        hand.addAll(deck.getFromTop(count));
        //TODO: Check loss
    }
    
    public void purgeFromDeck(int count) {
        voidPile.addAll(deck.getFromTop(count));
        //TODO: Check loss
    }

    public void discard(ArrayList<UUID> cards){
        cards.stream().map((cardID) -> getCardFrom(cardID, hand))
                .forEachOrdered((card) -> { scrapyard.add(card); });     
    }
    
    public void mulligan(){
        int size = hand.size();
        if(size > 0){
            deck.deck.addAll(hand);
            hand = new ArrayList<>();
            deck.shuffle();
            draw(size -1);
        }
    }

    public void newTurn(){
        energy = maxEnergy;
        numOfStudiesLeft = 1;
        playArea.forEach((card) -> card.depleted = false);
    }
    
    public void addKnowledge(Hashmap<Knowledge, Integer> knowl){
        knowl.forEach((k, i) -> {
            knowledgePool.merge(k, i, (a, b) -> a + b);
        });
        
    }

    public boolean hasKnowledge(Hashmap<Knowledge, Integer> cardKnowledge){
        boolean result = true; 
        result = cardKnowledge.keySet().stream().map((k) -> knowledgePool.containsKey(k) && 
                (cardKnowledge.get(k) <= knowledgePool.get(k))).reduce(result, (accumulator, _item) -> accumulator & _item);
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("xx Player xx\n");
        sb.append("  Player Name: ").append(name).append("\n");
        sb.append("  ID: ").append(id).append("\n");
        sb.append("  Energy: ").append(energy).append(" / ").append(maxEnergy).append("\n");
        sb.append("  Knowledge\n");
        //knowledgePool.forEach((k, c) ->{
        //    sb.append("    ").append(k).append(": ").append(c).append("\n");
        //});
        sb.append("  Source Play Count: ").append(numOfStudiesLeft).append("\n");
        sb.append("  Hand \n").append(list(hand, "    ")).append("\n");
        sb.append("  Items \n").append(list(playArea, "    ")).append("\n");
        sb.append("  Discard Pile \n").append(list(scrapyard, "    ")).append("\n");
        sb.append("  ").append(deck).append("\n");
        return sb.toString();
    }
    /*
    public void addTriggerEvent(Game game, Event e) {
        for (int i = 0; i < triggeringCards.size(); i++){
            triggeringCards.get(i).checkEvent(game, e);
        }
    }
    */
    public void purgeCardsFromHand(ArrayList<UUID> cards) {
        /*
        cards.stream().map((cardID) -> getCardFromHand(cardID)).map((card) -> {
            hand.remove(card);
            return card;
        }).forEachOrdered((card) -> {
            voidPile.add(card);
        });
        */
    }

    public Card getCardFrom (UUID cardID, ArrayList<Card> list){
        for (Card card : list) {
            if(card.id.equals(cardID)){
                list.remove(card);
                return card;
            }
        }
        return null;
    }
    
    public Card getCard(UUID cardID) {
        Card c; 
        ArrayList<ArrayList<Card>> lists = new ArrayList<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard); 
        lists.add(voidPile);
        lists.add(deck.deck);
        for (ArrayList<Card> list : lists){ 
            c = getCardFrom (cardID, list);
            if (c != null) {
                return c;
            }
        }
        return null;
    }
    
    public Card peekCardFrom (UUID cardID, ArrayList<Card> list){
        for (Card card : list) {
            if(card.id.equals(cardID)){ 
                return card;
            }
        }
        return null;
    }
    
    public Card peekCard(UUID cardID) {
        Card c; 
        ArrayList<ArrayList<Card>> lists = new ArrayList<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard); 
        lists.add(voidPile);
        lists.add(deck.deck);
        for (ArrayList<Card> list : lists){ 
            c = peekCardFrom (cardID, list);
            if (c != null) {
                return c;
            }
        }
        return null;
    }
}
