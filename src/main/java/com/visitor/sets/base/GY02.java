/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.*;

/**
 * @author pseudo
 */
public class GY02 extends Unit {

	public GY02 (Game game, String owner) {
		super(game, "GY02",
				2, new CounterMap(YELLOW, 1).add(GREEN, 1),
				"Whenever {~} attacks, it gets +1/+1 until end of turn for each ready unit you control.",
				2, 2,
				owner);


		triggering = new Triggering(game, this).addAttackChecker(this,
				event ->
						game.addToStack(new AbilityCard(game, this, "Whenever {~} attacks, it gets +1/+1 until end of turn for each ready unit you control.",
								() ->
										game.addTurnlyAttackAndHealth(id,
												game.countInZone(controller, Game.Zone.Play, Predicates.and(Predicates::isUnit, Predicates::isReady)),
												game.countInZone(controller, Game.Zone.Play, Predicates.and(Predicates::isUnit, Predicates::isReady)))
						))
		);
	}
}
