/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.game;

import com.visitor.card.Card;
import com.visitor.helpers.Arraylist;

import static com.visitor.game.Event.EventType.*;

/**
 * @author pseudo
 */
public class Event {


	public EventType type;
	public Arraylist<Object> data;

	private Event (EventType l) {
		type = l;
		data = new Arraylist<>();
	}

	//Creators
	public static Event draw (String username, Card drawn) {
		Event e = new Event(DRAW);
		e.data.add(username);
		e.data.add(drawn);
		return e;
	}

	public static Event discard (String username, Card discarded) {
		Event e = new Event(DISCARD);
		e.data.add(username);
		e.data.add(discarded);
		return e;
	}

	public static Event possess (String oldController, String newController, Card c) {
		Event e = new Event(POSSESS);
		e.data.add(oldController);
		e.data.add(newController);
		e.data.add(c);
		return e;
	}

	public static Event donate (String oldController, String newController, Card c) {
		Event e = new Event(DONATE);
		e.data.add(oldController);
		e.data.add(newController);
		e.data.add(c);
		return e;
	}

	public static Event turnStart (String turnPlayer) {
		Event e = new Event(TURN_START);
		e.data.add(turnPlayer);
		return e;
	}

	public static Event turnEnd (String turnPlayer) {
		Event e = new Event(TURN_END);
		e.data.add(turnPlayer);
		return e;
	}

	public static Event cleanup (String turnPlayer) {
		Event e = new Event(CLEANUP);
		e.data.add(turnPlayer);
		return e;
	}

	public static Event study (String studyingPlayer, Card studiedCard) {
		Event e = new Event(STUDY);
		e.data.add(studyingPlayer);
		e.data.add(studiedCard);
		return e;
	}

	public static Event destroy (Card destroyingCard, Card destroyedCard) {
		Event e = new Event(DESTROY);
		e.data.add(destroyingCard);
		e.data.add(destroyedCard);
		return e;
	}

	public static Event transform (Card transformingCard, Card transformedFrom, Card transformedTo) {
		Event e = new Event(TRANSFORM);
		e.data.add(transformingCard);
		e.data.add(transformedFrom);
		e.data.add(transformedTo);
		return e;
	}

	//Checkers
	public static boolean playersTurnStart (Event event, String playerName) {
		return (event.type == TURN_START && event.data.get(0).equals(playerName));
	}

	public enum EventType {
		POSSESS, TURN_START, TURN_END, CLEANUP,
		DRAW, DISCARD, STUDY,
		DESTROY, TRANSFORM, DONATE
	}

}
