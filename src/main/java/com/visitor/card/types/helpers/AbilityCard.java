/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.card.properties.Playable;
import com.visitor.game.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.protocol.Types;

import java.util.UUID;

/**
 * @author pseudo
 */
public class AbilityCard extends Card {

	public AbilityCard (Game game, Card creator, String text, Runnable effect, Arraylist<UUID> targets) {
		super(game, creator.name + "'s Ability", new CounterMap<>(), CardType.Ability, text, creator.controller);
		this.targets = new Arraylist<>(creator.id).putAllIn(targets);

		playable = new Playable(game, this).addResolveEffect(effect).setDisappearing();

	}

	public AbilityCard (Game game, Card creator, String text, Runnable effect, UUID... targets) {
		this(game, creator, text, effect, new Arraylist<>(targets));
	}

	public AbilityCard (Game game, Card creator, ActivatedAbility activatedAbility) {
		this(game, creator, activatedAbility.getText(), activatedAbility.getActivate(), activatedAbility.getTargets());
		this.id = activatedAbility.id;
	}

	@Override
	public Types.Card.Builder toCardMessage () {
		return super.toCardMessage()
				.setCost("");
	}

}
