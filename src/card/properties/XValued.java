/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.properties;

import client.Client;

/**
 * Interface for cards that has X associated with a cost.
 * @author pseudo
 */
public interface XValued {
    
    /**
     * CALLER: Client<br>
     * BEHAVIOR: Return true if argument is a valid X value.
     * @param c
     * @param x
     * @return
     */
    public abstract boolean isXValid(Client c, int x);
}
