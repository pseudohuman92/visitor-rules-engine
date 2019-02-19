
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.set1.black.*;
import static helpers.Debug.list;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import static java.lang.Integer.parseInt;
import java.lang.reflect.Type;
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
    
    public Deck(String username, boolean test){
        //generator = new CardGenerator("");
        deck = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            deck.add(new BA01(username));
            deck.add(new BA03(username));
            deck.add(new BI01(username));
            deck.add(new BI02(username));
            deck.add(new BI03(username));
        }
    }

    public Deck(String username){
        //generator = new CardGenerator(username);
        deck = new ArrayList<>();
    }

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
  
    public ArrayList<Card> extractFromTop(int count){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && !deck.isEmpty(); i++){
            cards.add(deck.remove(0));
        }
        return cards;
    }
    
    public Card extractFromTop(Class c){
        for (int i = 0; i < deck.size(); i++){
            if(c.isInstance(deck.get(i))){
                return deck.remove(i);
            }
        }
        return null;
    }
    
    public ArrayList<Card> getFromTop(int count){
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

    public void putTo(Card card, int index){
        deck.add(index, card);
    }
    
    public void putToBottom(Card card){
        putTo(card, deck.size()-1);
    }
    
    public void putToTop(Card card){
        putTo(card, 0);
    }

    public void putAllTo(ArrayList<Card> cards, int index){
        deck.addAll(index, cards);
    }
    
    public void putAllToBottom(ArrayList<Card> cards){
        putAllTo(cards, deck.size()-1);
    }
    
    public void putAllToTop(ArrayList<Card> cards){
        putAllTo(cards, 0);
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
