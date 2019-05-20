
package com.visitor.game;

import com.visitor.card.types.Card;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import static java.lang.Integer.parseInt;
import java.security.SecureRandom;
import com.visitor.helpers.Arraylist;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pseudo
 */
public class Deck extends Arraylist<Card> implements Serializable {

    public Deck(String userId){}

    public Deck(String userId, String[] decklist) {
        for (int i = 0; i < decklist.length; i++){
            String[] tokens = decklist[i].split(";");
            int count = parseInt(tokens[0]);
            String name = tokens[1];
            for (int j = 0; j < count; j++){
                add(Deck.createCard(userId, name));
            }
        }
    }
    
    public static Card createCard(String userId, String cardName) {
        try {
            Class<?> cardClass = Class.forName("com.visitor.set1."+cardName);
            Constructor<?> cardConstructor = cardClass.getConstructor(String.class);
            Object card = cardConstructor.newInstance(new Object[] { userId });
            return ((Card)card);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Deck.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
