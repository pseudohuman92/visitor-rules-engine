/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.card.Card;
import com.visitor.card.properties.Triggering;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author pseudo
 */
public class Effect extends Card {

	public Effect (Game game, Card creator, Consumer<Event> effect, UUID... targets) {
		this(game, creator, effect, new Arraylist<>(targets));
	}

	public Effect (Game game, Card creator, Consumer<Event> effect, Arraylist<UUID> targets) {
		super(game, "", new CounterMap<>(), CardType.Effect, "", creator.controller);
		this.targets = new Arraylist<>(creator.id).putAllIn(targets);

		triggering = new Triggering(game, this, effect).register();
	}
}
