/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.card.Card;
import enums.Knowledge;
import helpers.Hashmap;

/**
 * Abstract class for the Ally card type.
 * @author pseudo
 */
public abstract class Ally extends Card {
    
    public Ally(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, image, owner);
    }
    
}
