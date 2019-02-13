
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.card.Card;
import com.ccg.ancientaliens.game.Game;
import helpers.Hashmap;

/**
 * Abstract class for the Tome card type.
 * @author pseudo
 */
public abstract class Tome extends Card {
    
    /**
     *
     * @param name
     * @param text
     * @param image
     * @param owner
     */
    public Tome(String name, String text, String image, String owner) {
        super(name, 0, new Hashmap<>(), text, "assets/tome.png", owner);
    }

    @Override
    public boolean canPlay(Game game){ return false; }
    @Override
    public void resolve(Game game){}
}