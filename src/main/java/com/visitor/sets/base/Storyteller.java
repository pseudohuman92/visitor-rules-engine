/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;

/**
 * @author pseudo
 */
public class Storyteller extends Unit {

	public Storyteller (Game game, String owner) {
		super(game, "Storyteller",
				5, new CounterMap(),
				"{D}: {~} deals 1 damage to any target.\n" +
						"{2}, {D}: Draw a card.\n" +
						"{5}: Ready {~}.",
				4, 4,
				owner);

		activatable.addActivatedAbility(new ActivatedAbility(game, this, 0, "{D}: {~} deals 1 damage to any target.")
				.setTargeting(Game.Zone.Both_Play, Predicates::isUnit, 1, false,
						targetId -> game.dealDamage(id, targetId, 1))
				.setDepleting());

		activatable.addActivatedAbility(new ActivatedAbility(game, this, 2, "{2}, {D}: Draw a card.",
				()-> game.draw(controller, 1))
				.setDepleting());

		activatable.addActivatedAbility(new ActivatedAbility(game, this, 5, "{5}: Ready {~}.",
				()-> game.ready(id)));
	}
}
