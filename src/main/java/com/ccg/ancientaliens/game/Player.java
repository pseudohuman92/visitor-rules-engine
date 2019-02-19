package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.properties.Triggering;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.protocol.Types;
import com.ccg.ancientaliens.protocol.Types.KnowledgeGroup;
import com.ccg.ancientaliens.protocol.Types.*;
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
        hand.addAll(deck.extractFromTop(count));
    }
    
    public void purgeFromDeck(int count) {
        voidPile.addAll(deck.extractFromTop(count));
    }

    public void discard(ArrayList<UUID> cards){
        cards.stream().map((cardID) -> extractCardFrom(cardID, hand))
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
        //result = (boolean) cardKnowledge.keySet().stream().map((<any> k) -> (cardKnowledge.get(k) <= knowledgePool.get(k)) && 
        //        knowledgePool.containsKey(k)).reduce(result, (accumulator, _item) -> accumulator & _item);
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
        
        cards.stream().map((cardID) -> extractCardFrom(cardID, hand)).forEachOrdered((card) -> {
            voidPile.add(card);
        });
    }

    public Card extractCardFrom (UUID cardID, ArrayList<Card> list){
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
        ArrayList<ArrayList<Card>> lists = new ArrayList<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard); 
        lists.add(voidPile);
        lists.add(deck.deck);
        for (ArrayList<Card> list : lists){ 
            c = extractCardFrom (cardID, list);
            if (c != null) {
                return c;
            }
        }
        return null;
    }
    
    public Card getCardFrom (UUID cardID, ArrayList<Card> list){
        for (Card card : list) {
            if(card.id.equals(cardID)){ 
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

    public Types.Player.Builder toPlayerMessage() {
        Types.Player.Builder b = Types.Player.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setDeckSize(deck.size())
                .setEnergy(energy)
                .setMaxEnergy(maxEnergy);
        for(int i = 0; i < hand.size(); i++){
            b.addHand(hand.get(i).toCardMessage());
        }
        for(int i = 0; i < playArea.size(); i++){
            b.addPlay(playArea.get(i).toCardMessage());
        }
        for(int i = 0; i < scrapyard.size(); i++){
            b.addScrapyard(scrapyard.get(i).toCardMessage());
        }
        for(int i = 0; i < voidPile.size(); i++){
            b.addVoid(voidPile.get(i).toCardMessage());
        }
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
                .setHandSize(hand.size());
        for(int i = 0; i < playArea.size(); i++){
            b.addPlay(playArea.get(i).toCardMessage());
        }
        for(int i = 0; i < scrapyard.size(); i++){
            b.addScrapyard(scrapyard.get(i).toCardMessage());
        }
        for(int i = 0; i < voidPile.size(); i++){
            b.addVoid(voidPile.get(i).toCardMessage());
        }
        knowledgePool.forEach((k, i) -> {
            b.addKnowledgePool(KnowledgeGroup.newBuilder()
                    .setKnowledge(k)
                    .setCount(i).build());
        });
        return b;
    }

    public boolean hasAnItem() {
        for(Card c : playArea){
            if (c instanceof Item){
                return true;
            }
        }
        return false;
    }
}
