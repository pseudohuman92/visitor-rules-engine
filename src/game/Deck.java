/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import cards.Card;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
//import cards.CardGenerator;
import helpers.Debug;

/**
 *
 * @author pseudo
 */
public class Deck implements Serializable {
    //CardGenerator generator;
    ArrayList<Card> deck;
    
    public Deck(){
        //generator = new CardGenerator("");
        deck = new ArrayList<>();
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
            e.printStackTrace();
        }
        //Skip file name
        deckFile.nextLine();

        while (deckFile.hasNextLine()) {
            String card = deckFile.nextLine();
            int count = Integer.parseInt(card.substring(0, 1));
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
    
    public ArrayList<Card> draw(int count){
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < count && !deck.isEmpty(); i++){
            cards.add(deck.remove(0));
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
    
    public void insertAll(ArrayList<Card> cards, int index){
        deck.addAll(index, cards);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("__ Deck __\n").append(Debug.list(deck, "    "));
        return sb.toString();
    }
 }
