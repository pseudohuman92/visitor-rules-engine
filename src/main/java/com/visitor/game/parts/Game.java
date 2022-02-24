package com.visitor.game.parts;

import com.visitor.helpers.Arraylist;

import static com.visitor.protocol.Types.Phase.REDRAW;
import static java.lang.Math.random;
import static java.lang.System.out;


/**
 * @author pseudo
 * Base -> Getters -> Extractors -> Putters -> Checkers -> HigherOrder
 * Connections -> Messaging -> Events ->
 * Stack ->  Combat ->  CardDelegators -> PlayerDelegators
 * Actions -> Turns -> ClientActions -> Game
 */
public class Game extends ClientActions {

    //This needs to be called to start the game.
    public void addPlayers(com.visitor.game.Player p1, com.visitor.game.Player p2) {
        triggeringCards.put(p1.id, new Arraylist<>());
        triggeringCards.put(p2.id, new Arraylist<>());

        players.put(p1.id, p1);
        players.put(p2.id, p2);

        p1.deck.shuffle();
        p2.deck.shuffle();

        phase = REDRAW;
        turnPlayer = (random() < 0.5) ? p1.id : p2.id;
        activePlayer = turnPlayer;
        turnCount = 0;
        passCount = 0;
        p1.draw(5);
        p2.draw(5);
        out.println("Updating players from Game addPlayers. AP: " + activePlayer);
        updatePlayers();
        startActiveClock();
    }

    public void stopActiveClock() {
        if (activePlayer != null) {
            getPlayer(activePlayer).clock.pause();
        }
    }

    public void startActiveClock() {
        if (activePlayer != null) {
            getPlayer(activePlayer).clock.activate();
        }
    }

    /**
     * Transformation Methods
     * To change one card to another in-place.

     private void replaceWith (Card oldCard, Card newCard) {
     players.values().forEach(p -> p.replaceCardWith(oldCard, newCard));
     for (int i = 0; i < stack.size(); i++) {
     if (stack.get(i).equals(oldCard)) {
     stack.remove(i);
     stack.add(i, newCard);
     }
     }
     }

     public void transformTo (Card transformingCard, Card transformedCard, Card transformTo) {
     replaceWith(transformedCard, transformTo);
     addEvent(Event.transform(transformingCard, transformedCard, transformTo));
     }


     public void transformToJunk (Card transformingCard, UUID cardID) {
     Card card = getCard(cardID);
     Junk junk = new Junk(this, card.controller);
     junk.copyPropertiesFrom(card);
     transformTo(transformingCard, card, junk);
     }
     */

}
