
package com.visitor.game;

import com.visitor.card.types.Card;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import static java.lang.Integer.parseInt;
import java.security.SecureRandom;
import com.visitor.helpers.Arraylist;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author pseudo
 */
public class Deck extends Arraylist<Card> implements Serializable {

    public Deck(String userId){
    }

    public Deck(File file, String userId) {

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
  
    public Arraylist<Card> extractFromTop(int count){
        Arraylist<Card> cards = new Arraylist<>();
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
    
    public Arraylist<Card> getFromTop(int count){
        Arraylist<Card> cards = new Arraylist<>();
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

    public void putTo(Arraylist<Card> cards, int index){
        addAll(index, cards);
    }
    
    public void putToBottom(Arraylist<Card> cards){
        putTo(cards, size()-1);
    }
    
    public void putToTop(Arraylist<Card> cards){
        putTo(cards, 0);
    }
 
    public void shuffleInto(Arraylist<Card> cards) {
        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < cards.size(); i++){
            add(rand.nextInt(size()), cards.get(i));
        }
    }
 }
