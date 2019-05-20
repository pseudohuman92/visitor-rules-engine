/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.game;

import com.visitor.set1.*;
import java.util.Random;

/**
 *
 * @author pseudo
 */
public class TestDecks {
    
    static final int numOfDecks = 4;
    
    public static Deck randomDeck(String userId){
        Random r = new Random();
        switch (r.nextInt(numOfDecks - 1)){
            case 0:
                return blackDeck(userId);
            case 1:
                return blueDeck(userId);
            case 2:
                return redDeck(userId);
            case 3:
                return yellowDeck(userId);
                
            default:
                return null;
        }
    }
    
    public static Deck blackDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new BA01(userId));
            d.add(new BA02(userId));
            d.add(new BA03(userId));
            d.add(new BA04(userId));
            d.add(new BA05(userId));
            
            d.add(new BI01(userId));
            d.add(new BI02(userId));
            d.add(new BI03(userId));
            d.add(new BI04(userId));
            d.add(new BI05(userId));
            d.add(new BI06(userId));
            d.add(new BI07(userId));
            d.add(new BI08(userId));
        }
        return d;
    }
    
    public static Deck blueDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new UA01(userId));
            d.add(new UA02(userId));
            d.add(new UA03(userId));
            d.add(new UA04(userId));
            d.add(new UA05(userId));
            
            d.add(new UI01(userId));
            d.add(new UI02(userId));
            d.add(new UI03(userId));
            d.add(new UI04(userId));
            d.add(new UI05(userId));
            d.add(new UI06(userId));
            d.add(new UI07(userId));
            d.add(new UI08(userId));
        }
        return d;
    }
    
    public static Deck yellowDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new YA01(userId));
            d.add(new YA02(userId));
            d.add(new YA03(userId));
            d.add(new YA04(userId));
            d.add(new YA05(userId));
            d.add(new YA06(userId));
            
            d.add(new YI01(userId));
            d.add(new YI02(userId));
            d.add(new YI03(userId));
            d.add(new YI04(userId));
            d.add(new YI05(userId));
            d.add(new YI06(userId));
        }
        return d;
    }
    
    public static Deck redDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new RA01(userId));
            d.add(new RA02(userId));
            d.add(new RA03(userId));
            d.add(new RA04(userId));
            d.add(new RA05(userId));
            d.add(new RA06(userId));
            
            d.add(new RI01(userId));
            d.add(new RI02(userId));
            d.add(new RI03(userId));
            d.add(new RI04(userId));
            d.add(new RI05(userId));
            d.add(new RI06(userId));
            d.add(new RI07(userId));
        }
        return d;
    }
}
