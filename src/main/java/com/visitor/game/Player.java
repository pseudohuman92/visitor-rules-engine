package com.visitor.game;

import com.visitor.card.properties.Combat;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.Damage;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;
import com.visitor.protocol.Types.KnowledgeGroup;

import java.util.UUID;
import java.util.function.Predicate;

import static java.util.UUID.randomUUID;

/**
 * @author pseudo
 */
public class Player {

	public final Game game;

	public String username;
	public UUID id;
	public int energy;
	public int maxEnergy;
	public int numOfStudiesLeft;
	public Deck deck;
	public Arraylist<Card> hand;
	public Arraylist<Card> discardPile;
	public Arraylist<Card> voidPile;
	public Arraylist<Card> playArea;
	public CounterMap<Knowledge> knowledgePool;
	public Combat combat;
	public Clock clock;

	public Player (Game game, String username, String[] decklist) {
		this.game = game;
		this.username = username;
		id = randomUUID();
		this.deck = new Deck(game, id, decklist);
		energy = 0;
		maxEnergy = 0;
		numOfStudiesLeft = 1;
		hand = new Arraylist<>();
		discardPile = new Arraylist<>();
		voidPile = new Arraylist<>();
		playArea = new Arraylist<>();
		knowledgePool = new CounterMap<>();
		combat = new Combat(game, null, 30);
		clock = new Clock(60 * 60, () -> game.gameEnd(id, false));
		clock.start();
	}

	public void draw (int count) {
		hand.addAll(deck.extractFromTop(count));
	}

	public void receiveDamage (int damageAmount, Card source) {
		combat.receiveDamage(new Damage(damageAmount, false, false), source);
		if (combat.getHealth() <= 0) {
			game.gameEnd(id, false);
		}
	}

	public void payHealth (int amount) {
		combat.loseHealth(amount);
		if (combat.getHealth() <= 0) {
			game.gameEnd(id, false);
		}
	}

	public Card discard (UUID cardId) {
		Card c = extractCardFromList(cardId, hand);
		discardPile.add(c);
		return c;
	}

	public Arraylist<Card> discardAll (Arraylist<UUID> cardIds) {
		Arraylist<Card> discarded = new Arraylist<>();
		cardIds.stream().map((cardID) -> extractCardFromList(cardID, hand))
				.forEachOrdered((card) -> {
					discarded.add(card);
					discardPile.add(card);
				});
		return discarded;
	}

	public void redraw () {
		int size = hand.size();
		if (size > 0) {
			deck.addAll(hand);
			hand.clear();
			deck.shuffle();
			draw(size - 1);
		}
	}

	public void newTurn () {
		energy = maxEnergy;
		numOfStudiesLeft = 1;
		playArea.forEach(Card::newTurn);
	}

	public void addKnowledge (CounterMap<Knowledge> knowledge) {
		knowledgePool.merge(knowledge);
	}

	public boolean hasKnowledge (CounterMap<Knowledge> cardKnowledge) {
		boolean result = true;
		for (Knowledge k : cardKnowledge.keySet()) {
			result = result && cardKnowledge.get(k) <= knowledgePool.getOrDefault(k, 0);
		}
		return result;
	}

	public Card extractCardFromList (UUID cardID, Arraylist<Card> list) {
		if (cardID == null) {
			System.out.println("CardID is NULL!");
		}
		for (Card card : list) {
			if (card == null) {
				System.out.println("Card is NULL!");
			}
			if (card.id.equals(cardID)) {
				list.remove(card);
				return card;
			}
		}
		return null;
	}

	public Card extractCard (UUID cardID) {
		Card c;
		Arraylist<Arraylist<Card>> lists = new Arraylist<>();
		lists.add(hand);
		lists.add(playArea);
		lists.add(discardPile);
		lists.add(voidPile);
		lists.add(deck);
		for (Arraylist<Card> list : lists) {
			c = extractCardFromList(cardID, list);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	public Card getCardFromList (UUID cardID, Arraylist<Card> list) {
		for (Card card : list) {
			if (card.id.equals(cardID)) {
				return card;
			}
		}
		return null;
	}

	public Card getCard (UUID cardID) {
		Card c;
		Arraylist<Arraylist<Card>> lists = new Arraylist<>();
		lists.add(hand);
		lists.add(playArea);
		lists.add(discardPile);
		lists.add(voidPile);
		lists.add(deck);
		for (Arraylist<Card> list : lists) {
			c = getCardFromList(cardID, list);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	void replaceCardWith (Card oldCard, Card newCard) {
		Arraylist<Arraylist<Card>> lists = new Arraylist<>();
		lists.add(hand);
		lists.add(playArea);
		lists.add(discardPile);
		lists.add(voidPile);
		lists.add(deck);
		for (Arraylist<Card> list : lists) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(oldCard)) {
					list.remove(i);
					list.add(i, newCard);
				}
			}
		}
	}

	public Types.Player.Builder toPlayerMessage (boolean player) {
		Types.Player.Builder builder = Types.Player.newBuilder()
				.setId(id.toString())
				.setUsername(username)
				.setDeckSize(deck.size())
				.setEnergy(energy)
				.setMaxEnergy(maxEnergy)
				.setShield(combat.getShield())
				.setHandSize(hand.size())
				.setHealth(combat.getHealth())
				.setTime(clock.getTimeLeftSeconds())
				.addAllPlay(playArea.transform(c -> c.toCardMessage().build()))
				.addAllDiscardPile(discardPile.transform(c -> c.toCardMessage().build()))
				.addAllVoid(voidPile.transform(c -> c.toCardMessage().build()));

		if (player) {
			builder.addAllHand(hand.transform(c -> c.toCardMessage().build()));
		}

		knowledgePool.forEach((k, i) -> builder.addKnowledgePool(KnowledgeGroup.newBuilder()
				.setKnowledge(k)
				.setCount(i).build()));
		return builder;
	}


	public void addHealth (int health) {
		combat.addAttackAndHealth(0, health);
	}

	public void endTurn () {
		playArea.forEach(Card::endTurn);
		combat.endTurn();
	}

	public void putToBottomOfDeck (Card card) {
		deck.putToBottom(card);
	}

	public void shuffleIntoDeck (Card[] cards) {
		deck.shuffleInto(cards);
	}

	public Arraylist<Card> extractFromTopOfDeck (int count) {
		return deck.extractFromTop(count);
	}

	public int getHandSize () {
		return hand.size();
	}

	public void shuffleDeck () {
		deck.shuffle();
	}

	public Card extractTopmostMatchingFromDeck (Predicate<Card> cardPredicate) {
		return deck.extractTopmost(cardPredicate);
	}

	public int getKnowledgeCount (Knowledge knowledge) {
		return knowledgePool.getOrDefault(knowledge, 0);
	}

	public boolean hasHealth (int i) {
		return combat.getHealth() >= i;
	}

	public void purgeFromDeck (int i) {
		Arraylist<Card> topCards = extractFromTopOfDeck(i);
		voidPile.addAll(topCards);
	}

	public Card discardAtRandom () {
		return hand.remove((int) (Math.random() * getHandSize()));
	}

	public Arraylist<Card> getFromTopOfDeck (int i) {
		return deck.getFromTop(i);
	}
}
