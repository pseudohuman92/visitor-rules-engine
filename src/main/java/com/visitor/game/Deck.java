package com.visitor.game;

import com.visitor.card.Card;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.UUID;
import java.util.TreeMap;
import java.util.Random;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

/**
 * @author pseudo
 */
public class Deck extends Arraylist<Card> {

    public Deck(UUID playerId) {
    }

    public Deck(Game game, UUID playerId, String[] decklist) {
        for (String s : decklist) {
            String[] tokens = s.split(";");
            int count = parseInt(tokens[0]);
            String name = tokens[1].replace(" ", "")
                    .replace("-", "")
                    .replace("'", "");
            for (int j = 0; j < count; j++) {
                Card c = HelperFunctions.createCard(game, playerId, name);
                if (c != null) {
                    c.zone = Game.Zone.Deck;
                    add(c);
                }
            }
        }
    }


    public Arraylist<Card> extractFromTop(int count) {
        Arraylist<Card> cards = new Arraylist<>();
        for (int i = 0; i < count && !isEmpty(); i++) {
            cards.add(extractTopmost(Predicates::any));
        }
        return cards;
    }

    public Card extractTopmost(Predicate<Card> pred) {
        for (int i = 0; i < size(); i++) {
            if (pred.test(get(i))) {
                Card c = remove(i);
                c.zone = Base.Zone.None;
                return c;
            }
        }
        return null;
    }

    public Arraylist<Card> getFromTop(int count) {
        Arraylist<Card> cards = new Arraylist<>();
        for (int i = 0; i < count && i < size(); i++) {
            cards.add(get(i));
        }
        return cards;
    }

    public Card getTopmost(Predicate<Card> pred) {
        for (Card card : this) {
            if (pred.test(card)) {
                return card;
            }
        }
        return null;
    }

    public void shuffle() {
        Collections.shuffle(this, new SecureRandom());
    }

    public void putToIndex(int index, Card... cards) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Invalid deck insertion index: " + index);
        }
        new Arraylist<>(cards).forEach(c-> c.zone = Base.Zone.Deck);
        addAll(index, cards);
    }

    public void putToBottom(Card... cards) {
        putToIndex(size(), cards);
    }

    public void putToTop(Card... cards) {
        putToIndex(0, cards);
    }

    public void shuffleInto(Card... cards) {
        shuffleInto(new SecureRandom(), cards);
    }

    void shuffleInto(Random rand, Card... cards) {
        for (Card card : cards) {
            card.zone = Base.Zone.Deck;
            add(rand.nextInt(size() + 1), card);
        }
    }

    public Arraylist<String> toDeckList() {
        Arraylist<String> decklist = new Arraylist<>();
        TreeMap<String, Integer> counts = new TreeMap<>();
        for (Card c : this) {
            String identifier = c.getClass().getName().substring("com.visitor.sets.".length());
            counts.merge(identifier, 1, Integer::sum);
        }
        counts.forEach((identifier, count) -> decklist.add(count + ";" + identifier));
        return decklist;
    }

    public CounterMap<Types.Knowledge> getDeckColors() {
        CounterMap<Types.Knowledge> colors = new CounterMap<>();
        this.forEach(c -> colors.merge(c.knowledge));
        return colors;
    }
}
