/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.properties;

import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;

import java.util.function.Consumer;

/**
 * Interface for cards that has a triggering effect.
 *
 * @author pseudo
 */
public class Triggering {

	private final Card card;
	private final Game game;

	private Arraylist<Consumer<Event>> eventCheckerList;


	public Triggering (Game game, Card card) {
		this.card = card;
		this.game = game;
		eventCheckerList = new Arraylist<>();
	}

	public Triggering (Game game, Card card, Consumer<Event> eventChecker) {
		this(game, card);
		eventCheckerList.add(eventChecker);
	}

	public final void checkEvent (Event event) {
		eventCheckerList.forEachInOrder(ec -> ec.accept(event));
	}

	public final Triggering register () {
		game.addTriggeringCard(card.controller, card);
		return this;
	}

	public final Triggering deregister () {
		game.removeTriggeringCard(card);
		return this;
	}

	//Adders
	public final Triggering addEventChecker (Consumer<Event> eventChecker) {
		eventCheckerList.add(eventChecker);
		return this;
	}

	//Adders
	public final Triggering addStartOfControllerTurnChecker (Runnable startTurnChecker) {
		Consumer<Event> eventChecker = event -> {
			if (event.playersTurnStart(card.controller)) {
				startTurnChecker.run();
			}
		};
		eventCheckerList.add(eventChecker);
		return this;
	}

	public final Triggering addStartOfOpponentTurnChecker (Runnable startTurnChecker) {
		Consumer<Event> eventChecker = event -> {
			if (event.playersTurnStart(game.getOpponentName(card.controller))) {
				startTurnChecker.run();
			}
		};
		eventCheckerList.add(eventChecker);
		return this;
	}

	public final Triggering addEndOfControllerTurnChecker (Runnable endTurnChecker) {
		Consumer<Event> eventChecker = event -> {
			if (event.playersTurnEnd(card.controller)) {
				endTurnChecker.run();
			}
		};
		eventCheckerList.add(eventChecker);
		return this;
	}

	public final Triggering addEndOfOpponentTurnChecker (Runnable endTurnChecker) {
		Consumer<Event> eventChecker = event -> {
			if (event.playersTurnEnd(game.getOpponentName(card.controller))) {
				endTurnChecker.run();
			}
		};
		eventCheckerList.add(eventChecker);
		return this;
	}

	public final Triggering addAttackChecker (Card attacker, Consumer<Event> attackChecker) {
		Consumer<Event> eventChecker = event -> {
			if (event.type == Event.EventType.Attack && ((Arraylist<Card>)event.data.get(0)).contains(attacker)) {
				attackChecker.accept(event);
			}
		};
		eventCheckerList.add(eventChecker);
		return this;
	}

	// Resetters
	public final void resetEventCheckerList () {
		eventCheckerList.clear();
	}
}
