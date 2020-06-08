/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.game.Card;
import com.visitor.card.properties.Playable;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

import java.util.UUID;

/**
 * @author pseudo
 */
public class Ability extends Card {

	public Ability (Game game, Card creator, String text, Runnable effect, UUID... targets) {
		this(game, creator, text, effect, new Arraylist<>(targets));
	}

	public Ability (Game game, Card creator, String text, Runnable effect, Arraylist<UUID> targets) {
		super(game, creator.name + "'s Ability", new CounterMap<>(), CardType.Ability, text, creator.controller);
		this.targets = new Arraylist<>(creator.id).putAllIn(targets);

		playable = new Playable(game, this).setResolveEffect(effect);
	}

	@Override
	public Types.Card.Builder toCardMessage () {
		return super.toCardMessage()
				.setCost("");
	}

}
