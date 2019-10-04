
package com.visitor.game;

import com.visitor.card.types.Card;
import com.visitor.helpers.Arraylist;
import static java.lang.Class.forName;
import static java.lang.Integer.parseInt;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.function.Predicate;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author pseudo
 */
public class Deck extends Arraylist<Card>  {

    public static Card createCard(String username, String cardName) {
        try {
            Class<?> cardClass = forName("com.visitor.sets.set1."+cardName);
            Constructor<?> cardConstructor = cardClass.getConstructor(String.class);
            Object card = cardConstructor.newInstance(new Object[] { username });
            return ((Card)card);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException ex) {
            getLogger(Deck.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

    public Deck(String username){}

    public Deck(String username, String[] decklist) {
        for (int i = 0; i < decklist.length; i++){
            String[] tokens = decklist[i].split(";");
            int count = parseInt(tokens[0]);
            String name = tokens[1].replace(" ","")
                    .replace("-", "")
                    .replace(".", "")
                    .replace("'", "");
            for (int j = 0; j < count; j++){
                add(createCard(username, name));
            }
        }
    }
    
  
    public Arraylist<Card> extractFromTop(int count){
        Arraylist<Card> cards = new Arraylist<>();
        for (int i = 0; i < count && !isEmpty(); i++){
            cards.add(remove(0));
        }
        return cards;
    }
    
    public Card extractTopmost(Predicate<Card> pred){
        for (int i = 0; i < size(); i++){
            if(pred.test(get(i))){
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
    
    public Card getTopmost(Predicate<Card> pred){
        for (int i = 0; i < size(); i++){
            if(pred.test(get(i))){
                return get(i);
            }
        }
        return null;
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
