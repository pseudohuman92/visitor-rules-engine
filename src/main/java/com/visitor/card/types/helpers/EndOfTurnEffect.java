/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.card.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;

import java.util.UUID;

/**
 * @author pseudo
 */
public class EndOfTurnEffect extends Effect {

    public EndOfTurnEffect(Game game, Card creator, Runnable effect, UUID ...targets) {
        this(game, creator, effect, new Arraylist<>(targets));
    }

    public EndOfTurnEffect(Game game, Card creator, Runnable effect, Arraylist<UUID> targets) {
        super(game, creator, (event)->{}, targets);
        triggering.addEventChecker((event) -> {
            if (event.type == Event.EventType.CLEANUP){
                effect.run();
                triggering.deregister();
            }
        });
    }
}
