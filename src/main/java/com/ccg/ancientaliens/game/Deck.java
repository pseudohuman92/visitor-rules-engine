
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.cards.TestCard;
import static helpers.Debug.list;
import helpers.Hashmap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import static java.lang.Integer.parseInt;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author pseudo
 */
public class Deck implements Serializable {
    //CardGenerator generator;
    ArrayList<Card> deck;
    
    /**
     *
     */
    public Deck(String username, boolean test){
        //generator = new CardGenerator("");
        deck = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            deck.add(new TestCard("TestCard", 1, new Hashmap<>(), "Test Text", "", username));
        }
    }
    
    /**
     *
     * @param username
     */
    public Deck(String username){
        //generator = new CardGenerator(username);
        deck = new ArrayList<>();
    }
    
    /**
     *
     * @param file
     * @param username
     */
    public Deck(File file, String username) {
        //generator = new CardGenerator(username);
        deck = new ArrayList<>();
        
        Scanner deckFile = null;
        try {
            deckFile = new Scanner(file);
        } catch (FileNotFoundException e) {
        }
        //Skip file name
        deckFile.nextLine();

        while (deckFile.hasNextLine()) {
            String card = deckFile.nextLine();
            int count = parseInt(card.substring(0, 1));
            String name = card.substring(2);
            for (int i = 0; i < count; i++){
                //deck.add(generator.createCard(name));
            }
        }
        deckFile.close();
    }
    
    public int size() {
        return deck.size();
    }
  
    public ArrayList<Card> getFromTop(int count){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && !deck.isEmpty(); i++){
            cards.add(deck.remove(0));
        }
        return cards;
    }
    
    public ArrayList<Card> lookFromTop(int count){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && i < deck.size(); i++){
            cards.add(deck.get(i));
        }
        return cards;
    }

    public void shuffle(){
        Collections.shuffle(deck, new SecureRandom());
    }

    public boolean valid() {
        return true;
    }

    public void insertTo(Card card, int index){
        deck.add(index, card);
    }
    
    public void putToBottom(Card card){
        insertTo(card, deck.size()-1);
    }
    
    public void putToTop(Card card){
        insertTo(card, 0);
    }

    public void insertAllTo(ArrayList<Card> cards, int index){
        deck.addAll(index, cards);
    }
    
    public void putAllToBottom(ArrayList<Card> cards){
        insertAllTo(cards, deck.size()-1);
    }
    
    public void putAllToTop(ArrayList<Card> cards){
        insertAllTo(cards, 0);
    }
    
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("__ Deck __\n").append(list(deck, "    "));
        return sb.toString();
    }
 }
