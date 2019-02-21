
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.card.types.Card;
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
public class Deck extends ArrayList<Card> implements Serializable {

    public Deck(String username){
    }

    public Deck(File file, String username) {

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
                //add(generator.createCard(name));
            }
        }
        deckFile.close();
    }
  
    public ArrayList<Card> extractFromTop(int count){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && !isEmpty(); i++){
            cards.add(remove(0));
        }
        return cards;
    }
    
    public Card extractInstanceFromTop(Class c){
        for (int i = 0; i < size(); i++){
            if(c.isInstance(get(i))){
                return remove(i);
            }
        }
        return null;
    }
    
    public ArrayList<Card> getFromTop(int count){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && i < size(); i++){
            cards.add(get(i));
        }
        return cards;
    }

    public void shuffle(){
        Collections.shuffle(this, new SecureRandom());
    }

    public boolean valid() {
        return true;
    }

    public void putTo(ArrayList<Card> cards, int index){
        addAll(index, cards);
    }
    
    public void putToBottom(ArrayList<Card> cards){
        putTo(cards, size()-1);
    }
    
    public void putToTop(ArrayList<Card> cards){
        putTo(cards, 0);
    }
 
    public void shuffleInto(ArrayList<Card> cards) {
        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < cards.size(); i++){
            add(rand.nextInt(size()), cards.get(i));
        }
    }
 }
