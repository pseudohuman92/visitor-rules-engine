/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.game;

import com.visitor.card.types.Card;
import com.visitor.helpers.Arraylist;

/**
 *
 * @author pseudo
 */
public class Event {
    
    public static String POSSESSION = "POSSESSION";
    public static String TURN_START = "TURN_START";
    public static String TURN_END = "TURN_END";
    public static String DRAW = "DRAW";
    public static String DISCARD = "DISCARD";
    
    public String label;
    public Arraylist<Object> eventData;
    
    private Event(String l){
        label = l;
        eventData = new Arraylist<>();
    }
    
    public static Event draw (String username, Arraylist<Card> drawn){
        Event e = new Event(DRAW);
        e.eventData.add(username);
        e.eventData.add(drawn);
        return e;
    }
    
    public static Event discard (String username, Arraylist<Card> discarded){
        Event e = new Event(DISCARD);
        e.eventData.add(username);
        e.eventData.add(discarded);
        return e;
    }
    
    public static Event possession (String oldController, String newController, Arraylist<Card> c){
        Event e = new Event(POSSESSION);
        e.eventData.add(oldController);
        e.eventData.add(newController);
        e.eventData.add(c);
        return e;
    }
    
    public static Event turnStart (String turnPlayer){
        Event e = new Event(TURN_START);
        e.eventData.add(turnPlayer);
        return e;
    }
    
    public static Event turnEnd (String turnPlayer){
        Event e = new Event(TURN_END);
        e.eventData.add(turnPlayer);
        return e;
    }

}
