/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.game;

import com.visitor.card.types.Card;
import static com.visitor.game.Event.EventType.*;
import com.visitor.helpers.Arraylist;

/**
 *
 * @author pseudo
 */
public class Event {
    
    public enum EventType {
        POSSESSION, TURN_START, TURN_END, DRAW, DISCARD, STUDY,
        DESTROY;
    }
    
    public static Event draw (String username, Arraylist<Card> drawn){
        Event e = new Event(DRAW);
        e.data.add(username);
        e.data.add(drawn);
        return e;
    }
    
    public static Event discard (String username, Arraylist<Card> discarded){
        Event e = new Event(DISCARD);
        e.data.add(username);
        e.data.add(discarded);
        return e;
    }
    
    public static Event possession (String oldController, String newController, Arraylist<Card> c){
        Event e = new Event(POSSESSION);
        e.data.add(oldController);
        e.data.add(newController);
        e.data.add(c);
        return e;
    }
    
    public static Event turnStart (String turnPlayer){
        Event e = new Event(TURN_START);
        e.data.add(turnPlayer);
        return e;
    }
    
    public static Event turnEnd (String turnPlayer){
        Event e = new Event(TURN_END);
        e.data.add(turnPlayer);
        return e;
    }
    
    public static Event study (String studyingPlayer, Card studiedCard){
        Event e = new Event(STUDY);
        e.data.add(studyingPlayer);
        e.data.add(studiedCard);
        return e;
    }
    
    public static Event destroy (Card destroyingCard, Card destroyedCard){
        Event e = new Event(DESTROY);
        e.data.add(destroyingCard);
        e.data.add(destroyedCard);
        return e;
    }
    
    public EventType type;
    public Arraylist<Object> data;
    private Event(EventType l){
        type = l;
        data = new Arraylist<>();
    }

}
