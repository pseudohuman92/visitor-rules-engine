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
    
    public String label;
    public Arraylist<Object> eventData;
    
    private Event(String l){
        label = l;
        eventData = new Arraylist<>();
    }
    
    public static Event draw (String username, Arraylist<Card> drawn){
        Event e = new Event("Draw");
        e.eventData.add(username);
        e.eventData.add(drawn);
        return e;
    }
    
    public static Event possess (String oldController, String newController, Card c){
        Event e = new Event("Possess");
        e.eventData.add(oldController);
        e.eventData.add(newController);
        e.eventData.add(c);
        return e;
    }
    
    public static Event turnStart (String turnPlayer){
        Event e = new Event("Turn Start");
        e.eventData.add(turnPlayer);
        return e;
    }
    
    public static Event turnEnd (String turnPlayer){
        Event e = new Event("Turn End");
        e.eventData.add(turnPlayer);
        return e;
    }

}
