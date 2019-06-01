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
    
    //Event types
    public static final String POSSESSION = "POSSESSION";
    public static final String TURN_START = "TURN_START";
    public static final String TURN_END = "TURN_END";
    public static final String DRAW = "DRAW";
    public static final String DISCARD = "DISCARD";
    
    public String type;
    public Arraylist<Object> data;
    
    private Event(String l){
        type = l;
        data = new Arraylist<>();
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

}
