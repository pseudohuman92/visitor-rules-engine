package com.visitor.game;

import com.visitor.card.types.Card;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.KnowledgeGroup;
import com.visitor.protocol.Types.*;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.stream.Collectors;
import com.visitor.card.properties.Damageable;

/**
 *
 * @author pseudo
 */
public class Player implements Damageable {

    public String username;
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
    public int health;
    public int shield;
    public int reflect;
    
    /**
     *
     * @param username
     * @param deck
     */
    public Player (String username, String[] decklist){
        this.username = username;
        id = randomUUID();
        this.deck = new Deck(username, decklist);
        energy = 0;
        maxEnergy = 0;
        numOfStudiesLeft = 1;
        hand = new Arraylist<>();
        scrapyard = new Arraylist<>();
        voidPile = new Arraylist<>();
        playArea = new Arraylist<>();
        knowledgePool = new Hashmap<>();
        health = 30; 
        shield = 0;
        reflect = 0;
    }

    public void draw(int count){
        hand.addAll(deck.extractFromTop(count));
    }
    
    @Override
    public void dealDamage(Game game, int count, UUID source) {
        int damage = count;
        if(shield >= damage){
            shield -= damage;
            return;
        }
        damage -= shield;
        shield = 0;
        if(reflect >= damage){
            reflect -= damage;
            game.dealDamage(id, source, damage);
            return;
        }
        int temp = reflect;
        damage -= reflect;
        reflect = 0;
        health -= damage;
        game.dealDamage(id, source, temp);
    }

    public Arraylist<Card> discard(Arraylist<UUID> cards){
        Arraylist<Card> discarded = new Arraylist<>();
        cards.stream().map((cardID) -> extractCardFrom(cardID, hand))
                .forEachOrdered((card) -> { 
                    discarded.add(card);
                    scrapyard.add(card); });
        return discarded;
    }
    
    public void redraw(){
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
        playArea.forEach((card) -> card.ready());
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
                .setUserId(username)
                .setDeckSize(deck.size())
                .setEnergy(energy)
                .setMaxEnergy(maxEnergy)
                .setShield(shield)
                .setReflect(reflect)
                .setHandSize(hand.size())
                .setHealth(health)
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
    
    public Types.Player.Builder toOpponentMessage() {
        Types.Player.Builder b = Types.Player.newBuilder()
                .setId(id.toString())
                .setUserId(username)
                .setDeckSize(deck.size())
                .setEnergy(energy)
                .setMaxEnergy(maxEnergy)
                .setHandSize(hand.size())
                .setShield(shield)
                .setReflect(reflect)
                .setHealth(health)
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

    public void payLife(int damage) {
        health -= damage;
    }
    
}
