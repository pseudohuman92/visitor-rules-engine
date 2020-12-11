package com.visitor.card.types.helpers;

import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Predicates;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventChecker implements Consumer<Event> {
	private Predicate<Event> condition;
	private Consumer<Event> checker;
	private Card card;
	private Game game;

	public EventChecker(Game game, Card card, Consumer<Event> checker){
		this.game = game;
		this.card = card;
		this.checker = checker;
		condition = event -> true;
	}

	//Adders
	public final EventChecker addStartOfControllerTurnChecker () {
		return and(event -> event.playersTurnStart(card.controller));
	}

	public final EventChecker addStartOfOpponentTurnChecker () {
		return and(event -> event.playersTurnStart(game.getOpponentName(card.controller)));
	}

	public final EventChecker addEndOfControllerTurnChecker () {
		return and(event -> event.playersTurnEnd(card.controller));
	}

	public final EventChecker addEndOfOpponentTurnChecker () {
		return and(event -> event.playersTurnEnd(game.getOpponentName(card.controller)));
	}

	public final EventChecker addTypeChecker (Event.EventType type) {
		return and(event -> event.type == type);
	}

	public final EventChecker addCardListChecker (Predicate<Arraylist<Card>> predicate) {
		return and(event -> predicate.test((Arraylist<Card>)event.data.get(0)));
	}

	public final EventChecker addCardChecker (Predicate<Card> predicate) {
		return and(event -> predicate.test((Card)event.data.get(0)));
	}

	public final EventChecker addPlayerChecker (Predicate<String> predicate) {
		return and(event -> predicate.test((String)event.data.get(0)));
	}

	public final EventChecker and (Predicate<Event> predicate) {
		Predicate<Event> oldCondition = condition;
		condition = event -> oldCondition.test(event) && predicate.test(event);
		return this;
	}

	public final void accept(Event event){
		if (condition.test(event)) {
			checker.accept(event);
		}
	}
}
