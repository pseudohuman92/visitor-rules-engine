package com.visitor.game;

import com.visitor.helpers.Arraylist;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.Collections;
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
		for (int i = 0; i < decklist.length; i++) {
			String[] tokens = decklist[i].split(";");
			int count = parseInt(tokens[0]);
			String name = tokens[1].replace(" ", "")
					.replace("-", "")
					.replace("'", "");
			for (int j = 0; j < count; j++) {
				add(createCard(game, username, name));
			}
		}
	}

	public static Card createCard (Game game, String username, String cardName) {
		try {
			Class<?> cardClass = forName("com.visitor.sets." + cardName);
			Constructor<?> cardConstructor = cardClass.getConstructor(Game.class, String.class);
			Object card = cardConstructor.newInstance(game, username);
			return ((Card) card);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException ex) {
			getLogger(Deck.class.getName()).log(SEVERE, null, ex);
		}
		return null;
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
		for (int i = 0; i < size(); i++) {
			if (pred.test(get(i))) {
				return get(i);
			}
		}
		return null;
	}

	public void shuffle () {
		Collections.shuffle(this, new SecureRandom());
	}

	public void putToIndex (int index, Card ...cards) {
		addAll(index, cards);
	}

	public void putToBottom (Card ...cards) {
		putToIndex(size() - 1, cards);
	}

	public void putToTop (Card ...cards) {
		putToIndex(0, cards);
	}

	public void shuffleInto (Card ...cards) {
		SecureRandom rand = new SecureRandom();
		for (int i = 0; i < cards.length; i++) {
			add(rand.nextInt(size()), cards[i]);
		}
	}
}
