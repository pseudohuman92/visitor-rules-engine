/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.game;

import com.ccg.ancientaliens.enums.EventLabel;
import static com.ccg.ancientaliens.enums.EventLabel.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Event {
    
    public EventLabel label;
    public ArrayList<Object> eventData;
    
    private Event(EventLabel l){
        label = l;
        eventData = new ArrayList<>();
    }
    
    public static Event draw (String username, int count){
        Event e = new Event(DRAW);
        e.eventData.add(username);
        e.eventData.add(count);
        return e;
    }
    
    public static Event possess (String oldController, String newController, UUID cardID){
        Event e = new Event(POSSESS);
        e.eventData.add(oldController);
        e.eventData.add(newController);
        e.eventData.add(cardID);
        return e;
    }

}
