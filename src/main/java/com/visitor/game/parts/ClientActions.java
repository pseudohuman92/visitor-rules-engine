package com.visitor.game.parts;

import com.visitor.game.Card;
import com.visitor.game.Event;

import java.util.UUID;

public class ClientActions extends Turns {
    /**
     * Game Action Methods
     * These are game actions taken by a client
     */

    public void playCard(UUID playerId, UUID cardID, boolean withCost) {
        Card card = extractCard(cardID);
        card.play(withCost);
        addEvent(Event.playCard(card), true);
        activePlayer = getOpponentId(playerId);
    }

    public void playCard(UUID playerId, UUID cardID) {
        playCard(playerId, cardID, true);
    }

    public void playCardWithoutCost(UUID playerId, UUID cardID) {
        playCard(playerId, cardID, false);
    }

    public void activateCard(UUID playerId, UUID cardID) {
        getCard(cardID).activate();
        activePlayer = getOpponentId(playerId);
    }

    public void studyCard(UUID playerId, UUID cardID, boolean regular) {
        Card c = extractCard(cardID);
        c.study(getPlayer(playerId), regular);
        addEvent(Event.study(playerId, c), regular);
    }

    public void pass(UUID playerId) {
        passCount++;
        if (passCount == 2) {
            if (!stack.isEmpty()) {
                resolveStack();
            } else {
                changePhase();
            }
        } else {
            activePlayer = this.getOpponentId(playerId);
        }
    }

    public void redraw(UUID playerId) {
        getPlayer(playerId).redraw();
    }

    public void keep(UUID playerId) {
        passCount++;
        if (passCount == 2) {
            changePhase();
        } else {
            activePlayer = this.getOpponentId(playerId);
        }
    }
}
