/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.card.properties;

import com.ccg.ancientaliens.card.Card;

/**
 * Interface for cards that require targets for their effects.
 * @author pseudo
 */
public interface Targeting {

    /**
     * BEHAVIOR: Return true if the argument is a valid target for the card.
     * @param c
     * @return
     */
    public abstract boolean validTarget(Card c);
}
