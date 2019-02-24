package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.KnowledgeGroup;
import com.ccg.ancientaliens.protocol.Types.*;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.io.Serializable;
import com.ccg.ancientaliens.helpers.Arraylist;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.stream.Collectors;

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
    public Arraylist<Card> hand;
    public Arraylist<Card> scrapyard;
    public Arraylist<Card> voidPile;
    public Arraylist<Card> playArea;
    public Hashmap<Knowledge, Integer> knowledgePool;
    
    public int shield;
    public int reflect;
    
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
        hand = new Arraylist<>();
        scrapyard = new Arraylist<>();
        voidPile = new Arraylist<>();
        playArea = new Arraylist<>();
        knowledgePool = new Hashmap<>();
        shield = 0;
        reflect = 0;
    }

    public void draw(int count){
        hand.addAll(deck.extractFromTop(count));
    }
    
    public int purgeFromDeck(int count) {
        if(shield >= count){
            shield -= count;
            return 0;
        }
        count -= shield;
        shield = 0;
        if(reflect >= count){
            reflect -= count;
            return count;
        }
        int temp = reflect;
        count -= reflect;
        reflect = 0;
        voidPile.addAll(deck.extractFromTop(count));
        return temp;
    }

    public void discard(Arraylist<UUID> cards){
        cards.stream().map((cardID) -> extractCardFrom(cardID, hand))
                .forEachOrdered((card) -> { scrapyard.add(card); });     
    }
    
    public void mulligan(){
        int size = hand.size();
        if(size > 0){
            deck.addAll(hand);
            hand = new Arraylist<>();
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
        for (Knowledge k : cardKnowledge.keySet()){
            result = result && cardKnowledge.get(k) <= knowledgePool.getOrDefault(k, 0);
        }
        return result;
    }

    public Card extractCardFrom (UUID cardID, Arraylist<Card> list){
        for (Card card : list) {
            if(card.id.equals(cardID)){
                list.remove(card);
                return card;
            }
        }
        return null;
    }
    
    public Card extractCard(UUID cardID) {
        Card c; 
        Arraylist<Arraylist<Card>> lists = new Arraylist<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard); 
        lists.add(voidPile);
        lists.add(deck);
        for (Arraylist<Card> list : lists){ 
            c = extractCardFrom (cardID, list);
            if (c != null) {
                return c;
            }
        }
        return null;
    }
    
    public Card getCardFrom (UUID cardID, Arraylist<Card> list){
        for (Card card : list) {
            if(card.id.equals(cardID)){ 
                return card;
            }
        }
        return null;
    }
    
    public Card getCard(UUID cardID) {
        Card c; 
        Arraylist<Arraylist<Card>> lists = new Arraylist<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard); 
        lists.add(voidPile);
        lists.add(deck);
        for (Arraylist<Card> list : lists){ 
            c = getCardFrom (cardID, list);
            if (c != null) {
                return c;
            }
        }
        return null;
    }
    
    void replaceWith(Card oldCard, Card newCard) {
        Arraylist<Arraylist<Card>> lists = new Arraylist<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard); 
        lists.add(voidPile);
        lists.add(deck);
        for (Arraylist<Card> list : lists){ 
            for (int i = 0; i < list.size(); i++){
                if(list.get(i).equals(oldCard)){
                    list.remove(i);
                    list.add(i, newCard);
                }
            }
        }
    }

    public Types.Player.Builder toPlayerMessage() {
        Types.Player.Builder b = Types.Player.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setDeckSize(deck.size())
                .setEnergy(energy)
                .setMaxEnergy(maxEnergy)
                .setShield(shield)
                .setReflect(reflect)
                .addAllHand(hand.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .addAllPlay(playArea.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .addAllScrapyard(scrapyard.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .addAllVoid(voidPile.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()));

        knowledgePool.forEach((k, i) -> {
            b.addKnowledgePool(KnowledgeGroup.newBuilder()
                    .setKnowledge(k)
                    .setCount(i).build());
        });
        return b;
    }
    
    public Types.Opponent.Builder toOpponentMessage() {
        Types.Opponent.Builder b = Types.Opponent.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setDeckSize(deck.size())
                .setEnergy(energy)
                .setMaxEnergy(maxEnergy)
                .setHandSize(hand.size())
                .setShield(shield)
                .setReflect(reflect)
                .addAllPlay(playArea.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .addAllScrapyard(scrapyard.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()))
                .addAllVoid(voidPile.parallelStream().map(c->{return c.toCardMessage().build();}).collect(Collectors.toList()));
                
        knowledgePool.forEach((k, i) -> {
            b.addKnowledgePool(KnowledgeGroup.newBuilder()
                    .setKnowledge(k)
                    .setCount(i).build());
        });
        return b;
    }

    void purgeSelf(int count) {
        voidPile.addAll(deck.extractFromTop(count));    
    }
    
}
