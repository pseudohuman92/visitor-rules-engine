/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.types;

import card.Card;
import enums.Knowledge;
import enums.Type;
import helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public abstract class Ally extends Card {
    
    public Ally(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, Type type, String owner) {
        super(name, cost, knowledge, text, image, type, owner);
    }
    
}
