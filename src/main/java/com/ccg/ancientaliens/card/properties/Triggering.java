/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.card.properties;

import game.Event;
import com.ccg.ancientaliens.game.Game;

/**
 * Interface for cards that has a triggering effect.
 * @author pseudo
 */
public interface Triggering {
    
    /**
     * CALLER: Server<br>
     * BEHAVIOR: Check if the passed event satisfies the trigger conditions and execute the trigger if it does.
     * @param game
     * @param event
     */
    public void checkEvent(Game game, Event event);
}
