package game;

import cards.Item;
import cards.Card;
import enums.Knowledge;
import helpers.Hashmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Player implements Serializable {
    public String name;
    public UUID uuid;
    public int life;
    public Deck deck;
    public int energy;
    public int playableSource;
    public ArrayList<Card> sources;
    public ArrayList<Card> hand;
    public ArrayList<Card> discardPile;
    public ArrayList<Item> items;
    public Hashmap<Knowledge, Integer> knowledge;
    
    public Player (String name, Deck deck){
        this.name = name;
        uuid = UUID.randomUUID();
        life = 30;
        this.deck = deck;
        energy = 0;
        playableSource = 1;
        sources = new ArrayList<>();
        hand = new ArrayList<>();
        discardPile = new ArrayList<>();
        items = new ArrayList<>();
        knowledge = new Hashmap<>();
    }

    public Opponent toOpponent ()
    {
        return new Opponent(this);
    }
    
    public void draw(int count){
        hand.addAll(deck.draw(count));
    }
    
    public Card fromHand(UUID cardID){
        for (Card card : hand) {
            if(card.uuid.equals(cardID)){ 
                return card;
            }
        }
        return null;
    }
    
    public Item fromItems(UUID cardID){
        for (Item card : items) {
            if(card.uuid.equals(cardID)){ 
                return card;
            }
        }
        return null;
    }
    
    public void discard(ArrayList<UUID> cards){
        for (UUID cardID : cards) {
            Card card = fromHand(cardID);
            hand.remove(card);
            discardPile.add(card);
        }
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
        energy = sources.size();
        playableSource = 1;
        items.forEach((card) -> card.depleted = false);
    }
    
    public void addKnowledge(Hashmap<Knowledge, Integer> knowl){
        knowl.forEach((k, i) -> {
            knowledge.merge(k, i, (a, b) -> a + b);
        });
    }
    
    public boolean hasKnowledge(Hashmap<Knowledge, Integer> cardKnowledge){
        boolean result = true; 
        for (Knowledge k : cardKnowledge.keySet()){
            result &= knowledge.containsKey(k) && (cardKnowledge.get(k) <= knowledge.get(k));
        }
        return result;
    }
    
    
}
