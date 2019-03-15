/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.set1.black.*;
import com.ccg.ancientaliens.set1.blue.*;
import com.ccg.ancientaliens.set1.yellow.*;
import com.ccg.ancientaliens.set1.red.*;
import java.util.Random;

/**
 *
 * @author pseudo
 */
public class TestDecks {
    
    static final int numOfDecks = 4;
    
    public static Deck randomDeck(String username){
        Random r = new Random();
        switch (r.nextInt(numOfDecks - 1)){
            case 0:
                return blackDeck(username);
            case 1:
                return blueDeck(username);
            case 2:
                return redDeck(username);
            case 3:
                return yellowDeck(username);
                
            default:
                return null;
        }
    }
    
    public static Deck blackDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new BA01(username));
            d.add(new BA02(username));
            d.add(new BA03(username));
            d.add(new BA04(username));
            d.add(new BA05(username));
            
            d.add(new BI01(username));
            d.add(new BI02(username));
            d.add(new BI03(username));
            d.add(new BI04(username));
            d.add(new BI05(username));
            d.add(new BI06(username));
            d.add(new BI07(username));
            d.add(new BI08(username));
        }
        return d;
    }
    
    public static Deck blueDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new UA01(username));
            d.add(new UA02(username));
            d.add(new UA03(username));
            d.add(new UA04(username));
            d.add(new UA05(username));
            
            d.add(new UI01(username));
            d.add(new UI02(username));
            d.add(new UI03(username));
            d.add(new UI04(username));
            d.add(new UI05(username));
            d.add(new UI06(username));
            d.add(new UI07(username));
            d.add(new UI08(username));
        }
        return d;
    }
    
    public static Deck yellowDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new YA01(username));
            d.add(new YA02(username));
            d.add(new YA03(username));
            d.add(new YA04(username));
            d.add(new YA05(username));
            d.add(new YA06(username));
            
            d.add(new YI01(username));
            d.add(new YI02(username));
            d.add(new YI03(username));
            d.add(new YI04(username));
            d.add(new YI05(username));
            d.add(new YI06(username));
        }
        return d;
    }
    
    public static Deck redDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new RA01(username));
            d.add(new RA02(username));
            d.add(new RA03(username));
            d.add(new RA04(username));
            d.add(new RA05(username));
            d.add(new RA06(username));
            
            d.add(new RI01(username));
            d.add(new RI02(username));
            d.add(new RI03(username));
            d.add(new RI04(username));
            d.add(new RI05(username));
            d.add(new RI06(username));
            d.add(new RI07(username));
        }
        return d;
    }
}
