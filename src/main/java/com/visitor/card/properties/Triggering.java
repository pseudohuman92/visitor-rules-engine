/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.properties;

import com.visitor.game.Event;
import com.visitor.game.Game;

/**
 * Interface for cards that has a triggering effect.
 * @author pseudo
 */
public interface Triggering {

    public void checkEvent(Game game, Event event);
}
