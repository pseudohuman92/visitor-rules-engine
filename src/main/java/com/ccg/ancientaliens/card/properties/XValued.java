/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.properties;

/**
 * Interface for cards that has X associated with a cost.
 * @author pseudo
 */
public interface XValued {
    
    /**
     * BEHAVIOR: Return true if argument is a valid X value.
     * @param c
     * @param x
     * @return
     */
    public abstract boolean isXValid(int x);
}
