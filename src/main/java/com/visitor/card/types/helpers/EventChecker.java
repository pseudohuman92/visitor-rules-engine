package com.visitor.card.types.helpers;

import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventChecker implements Consumer<Event> {
	private Predicate<Event> condition;
	private Consumer<Event> checker;
	private Card card;
	private Game game;
	private boolean createAbility;
	private String abilityText;

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
		return and(event -> event.playersTurnStart(game.getOpponentId(card.controller)));
	}

	public final EventChecker addEndOfControllerTurnChecker () {
		return and(event -> event.playersTurnEnd(card.controller));
	}

	public final EventChecker addEndOfOpponentTurnChecker () {
		return and(event -> event.playersTurnEnd(game.getOpponentId(card.controller)));
	}

	public final EventChecker addTypeChecker (Event.EventType type) {
		return and(event -> event.type == type);
	}

	//Make sure that card list is the first argument of event.data before using.
	public final EventChecker addCardListChecker (Predicate<Arraylist<Card>> predicate) {
		return and(event -> predicate.test((Arraylist<Card>)event.data.get(0)));
	}

	//Make sure that card is the first argument of event.data before using.
	public final EventChecker addCardChecker (Predicate<Card> predicate) {
		return and(event -> predicate.test((Card)event.data.get(0)));
	}

	//Make sure that player name is the first argument of event.data before using.
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
			if (createAbility) {
				game.addToStack(new AbilityCard(game, card, abilityText, () -> checker.accept(event)));
			} else {
				checker.accept(event);
			}
		}
	}

	public EventChecker createAbility (String abilityText) {
		createAbility = true;
		this.abilityText = abilityText;
		return this;
	}
}
