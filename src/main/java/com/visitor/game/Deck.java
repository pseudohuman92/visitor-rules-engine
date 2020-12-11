package com.visitor.game;

import com.visitor.helpers.Arraylist;
import com.visitor.helpers.HelperFunctions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;

import static java.lang.Class.forName;
import static java.lang.Integer.parseInt;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 * @author pseudo
 */
public class Deck extends Arraylist<Card> {

	public Deck (String username) {
	}

	public Deck (Game game, String username, String[] decklist) {
		for (String s : decklist) {
			String[] tokens = s.split(";");
			int count = parseInt(tokens[0]);
			String name = tokens[1].replace(" ", "")
					.replace("-", "")
					.replace("'", "");
			for (int j = 0; j < count; j++) {
				Card c = HelperFunctions.createCard(game, username, name);
				if (c != null){
					c.zone = Game.Zone.Deck;
					add(c);
				}
			}
		}
	}


	public Arraylist<Card> extractFromTop (int count) {
		Arraylist<Card> cards = new Arraylist<>();
		for (int i = 0; i < count && !isEmpty(); i++) {
			cards.add(remove(0));
		}
		return cards;
	}

	public Card extractTopmost (Predicate<Card> pred) {
		for (int i = 0; i < size(); i++) {
			if (pred.test(get(i))) {
				return remove(i);
			}
		}
		return null;
	}

	public Arraylist<Card> getFromTop (int count) {
		Arraylist<Card> cards = new Arraylist<>();
		for (int i = 0; i < count && i < size(); i++) {
			cards.add(get(i));
		}
		return cards;
	}

	public Card getTopmost (Predicate<Card> pred) {
		for (Card card : this) {
			if (pred.test(card)) {
				return card;
			}
		}
		return null;
	}

	public void shuffle () {
		Collections.shuffle(this, new SecureRandom());
	}

	public void putToIndex (int index, Card... cards) {
		addAll(index, cards);
	}

	public void putToBottom (Card... cards) {
		putToIndex(size() - 1, cards);
	}

	public void putToTop (Card... cards) {
		putToIndex(0, cards);
	}

	public void shuffleInto (Card... cards) {
		SecureRandom rand = new SecureRandom();
		for (Card card : cards) {
			add(rand.nextInt(size()), card);
		}
	}

	public Arraylist<String> toDeckList () {
		Arraylist<String> decklist = new Arraylist<>();
		sort(Comparator.comparing(card -> card.name));
		String lastName = "";
		int count = 0;
		for (Card c : this) {
			if (!lastName.equals(c.name)) {
				if (!lastName.equals("")) {
					decklist.add(count + ";base." + lastName);
				}
				lastName = c.name;
				count = 1;
			} else {
				count++;
			}
		}
		if (!lastName.equals("")) {
			decklist.add(count + ";base." + lastName);
		}
		return decklist;
	}
}
