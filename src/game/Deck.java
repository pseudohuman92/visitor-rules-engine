
package game;

import card.Card;
import static helpers.Debug.list;
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
    public Deck(){
        //generator = new CardGenerator("");
        deck = new ArrayList<>();
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
    
    /**
     *
     * @return
     */
    public int size() {
        return deck.size();
    }
    
    /**
     *
     * @param count
     * @return
     */
    public ArrayList<Card> draw(int count){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && !deck.isEmpty(); i++){
            cards.add(deck.remove(0));
        }
        return cards;
    }
    
    /**
     *
     */
    public void shuffle(){
        Collections.shuffle(deck, new SecureRandom());
    }
    
    /**
     *
     * @return
     */
    public boolean valid() {
        return true;
    }
    
    /**
     *
     * @param card
     * @param index
     */
    public void insertTo(Card card, int index){
        deck.add(index, card);
    }
    
    /**
     *
     * @param cards
     * @param index
     */
    public void insertAll(ArrayList<Card> cards, int index){
        deck.addAll(index, cards);
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
