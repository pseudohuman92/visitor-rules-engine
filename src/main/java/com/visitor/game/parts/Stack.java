package com.visitor.game.parts;

import com.visitor.card.Card;

import java.util.UUID;

import static java.lang.System.out;

public class Stack extends Events {
    /**
     * Stack Methods
     */
    public void addToStack(Card c) {
        passCount = 0;
        c.zone = Zone.Stack;
        stack.add(0, c);
        processEvents();
    }

    // TODO: switch prevSize check to flag system
    void resolveStack() {
        activePlayer = UUID.randomUUID();
        out.println("Updating players from resolveStack beginning. AP: " + activePlayer);
        updatePlayers();
        while (!stack.isEmpty() && passCount == 2) {
            currentlyResolving = stack.remove(0);
            int prevSize = stack.size();
            currentlyResolving.resolve();
            processEvents();
            if (stack.isEmpty() || prevSize != stack.size()) {
                passCount = 0;
                activePlayer = turnPlayer;
            } else {
                out.println("Updating players from resolveStack loop. AP: " + activePlayer);
                updatePlayers();
            }
            currentlyResolving = null;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /*
    // This is stop after each resolution version.
    private void resolveStack() {
        if (passCount == 2) {
            activePlayer = " ";
            updatePlayers();
            Card c = stack.remove(0);
            c.resolve(this);
            passCount = 0;
            activePlayer = turnPlayer;
        }
    }
    */
}
