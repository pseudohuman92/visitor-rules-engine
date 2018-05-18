/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.gameobjects;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author pseudo
 */
public class Deck implements Serializable {
    ArrayList<Card> deck;
    
    public int size() {
        return deck.size();
    }
    
    public Card draw(){
        return deck.remove(0);
    }
    
    public void shuffle(){
        //Implement
    }
    
    public static Deck fromFile(File file) {
        return new Deck();
    }
    
    public boolean valid() {
        return true;
    }
}
