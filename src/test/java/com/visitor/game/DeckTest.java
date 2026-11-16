package com.visitor.game;

import com.visitor.card.Card;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.sets.base2.FlameStrike;
import com.visitor.sets.base2.Stormtrooper;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

public class DeckTest {
    private final Game game = new Game();
    private final UUID owner = UUID.randomUUID();
    private Card card() { return new Stormtrooper(game, owner); }

    @Test public void bottomInsertionAppendsAndPreservesBatchOrder() {
        Deck deck = new Deck(owner);
        Card a=card(), b=card(), c=card(), d=card();
        deck.putToBottom(a, b);
        deck.putToBottom(c, d);
        assertEquals(Arrays.asList(a,b,c,d), deck);
        for (Card card : deck) assertEquals(Base.Zone.Deck, card.zone);
        assertEquals(Arrays.asList(a,b,c,d), deck.extractFromTop(4));
    }

    @Test public void topInsertionWorksOnEmptyDeckAndPreservesBatchOrder() {
        Deck deck = new Deck(owner);
        Card a=card(), b=card(), c=card();
        deck.putToTop(c);
        deck.putToTop(a,b);
        assertEquals(Arrays.asList(a,b,c), deck.extractFromTop(3));
    }

    @Test public void emptyInsertionsAreNoOps() {
        Deck deck = new Deck(owner);
        deck.putToBottom(); deck.putToTop(); deck.shuffleInto();
        assertTrue(deck.isEmpty());
    }

    @Test public void invalidIndexDoesNotChangeCardZoneOrDeck() {
        Deck deck = new Deck(owner);
        Card card=card();
        assertThrows(IndexOutOfBoundsException.class, () -> deck.putToIndex(-1,card));
        assertThrows(IndexOutOfBoundsException.class, () -> deck.putToIndex(1,card));
        assertEquals(Base.Zone.None, card.zone);
        assertTrue(deck.isEmpty());
    }

    @Test public void randomInsertionWorksOnEmptyAndSingleCardDecks() {
        Deck deck = new Deck(owner);
        Card a=card(), b=card();
        deck.shuffleInto(a);
        assertSame(a,deck.get(0));
        deck.shuffleInto(b);
        assertEquals(2,deck.size());
        assertTrue(deck.containsAll(Arrays.asList(a,b)));
        for (Card card : deck) assertEquals(Base.Zone.Deck, card.zone);
    }

    @Test public void randomInsertionCanChooseBottomAndUpdatesBoundForEachCard() {
        Deck deck = new Deck(owner);
        Card a=card(), b=card(), c=card();
        deck.putToTop(a);
        deck.shuffleInto(new Random() {
            int expected=2;
            @Override public int nextInt(int bound) {
                assertEquals(expected++,bound);
                return bound-1;
            }
        }, b,c);
        assertEquals(Arrays.asList(a,b,c),deck);
    }

    @Test public void exportUsesRealClassIdentifiersWithoutReorderingDeck() {
        Deck deck = new Deck(owner);
        Card a=card(), b=new FlameStrike(game,owner), c=card();
        deck.putToBottom(a,b,c);
        assertEquals(Arrays.asList("1;base2.FlameStrike","2;base2.Stormtrooper"), deck.toDeckList());
        assertEquals(Arrays.asList(a,b,c),deck);
        Deck restored = new Deck(game, owner, deck.toDeckList().toArray(new String[0]));
        assertEquals(3,restored.size());
        assertEquals(deck.toDeckList(),restored.toDeckList());
    }
}
