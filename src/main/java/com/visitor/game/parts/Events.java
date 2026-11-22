package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.game.Event;
import com.visitor.helpers.Arraylist;

import java.util.UUID;

import static com.visitor.game.Event.turnEnd;
import static com.visitor.game.Event.turnStart;
import static java.lang.System.out;

public class Events extends Messaging {
    private int eventProcessingDepth;
    /**
     * Event Related Methods
     * These are the methods that implements event mechanism.
     * Events are used to implement triggered abilities.
     */
    public void addEvent(Event e, boolean process) {
        eventQueue.add(e);
        if (process)
            processEvents();
    }

    public void addEvent(Event e) {
        addEvent(e, false);
    }

    /**
     * Current batch: FIFO events, turn player's cards before opponent's cards,
     * then card/checker registration order. Explicit nested processing runs inline.
     */
    public void processEvents() {
        eventProcessingDepth++;
        try {
            if (!eventQueue.isEmpty()) {
                Arraylist<Event> tempQueue = eventQueue;
                eventQueue = new Arraylist<>();
                while (!tempQueue.isEmpty()) {
                    Event e = tempQueue.remove(0);
                    out.println("Processing Event: " + e.type);
                    new Arraylist<>(triggeringCards.get(turnPlayer)).forEach(c -> c.checkEvent(e));
                    new Arraylist<>(triggeringCards.get(this.getOpponentId(turnPlayer))).forEach(c -> c.checkEvent(e));
                }
            }
        } finally {
            if (--eventProcessingDepth == 0) {
                toDeregister.forEach(Card::deregister);
                toDeregister.clear();
            }
        }
    }

    void processBeginEvents() {
        //out.println("Starting Begin Triggers");
        addEvent(turnStart(turnPlayer), true);
        //out.println("Ending Begin Triggers");
    }

    void processEndEvents() {
        //out.println("Starting End Triggers");
        addEvent(turnEnd(turnPlayer), true);
        //out.println("Ending End Triggers");
    }

    public void addTriggeringCard(UUID playerId, Card t) {
        triggeringCards.get(playerId).add(t);
    }

    public void removeTriggeringCard(Card card) {
        triggeringCards.values().forEach(l -> l.remove(card));
    }

    public void addToDeregister(Card card) {
        toDeregister.add(card);
    }
}
