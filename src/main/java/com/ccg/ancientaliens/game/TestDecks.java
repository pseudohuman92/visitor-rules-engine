/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.set1.black.*;
import com.ccg.ancientaliens.set1.blue.*;
import com.ccg.ancientaliens.set1.yellow.*;

/**
 *
 * @author pseudo
 */
public class TestDecks {
    
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
            
            d.add(new YI01(username));
            d.add(new YI02(username));
            d.add(new YI03(username));
        }
        return d;
    }
}
