/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.properties;

import card.Card;

/**
 *
 * @author pseudo
 */
public interface Targeting {

    /**
     *
     * @param c
     * @return
     */
    public abstract boolean validTarget(Card c);
}
