/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.properties.Combat;
import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.sets.token.UnitToken;

import java.util.ArrayList;
import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.GREEN;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

/**
 * @author pseudo
 */
public class GY04 extends Unit {

	public GY04 (Game game, String owner) {
		super(game, "GY04",
				5, new CounterMap(YELLOW, 1).add(GREEN, 2),
				"When {~} enters play, create a 3/3 green and white Wolf.\n" +
				"Whenever a Wolf enters play under your control, \n" +
				"you gain 3 life and that unit fights up to one target unit you don’t control.",
				3, 3,
				owner, Combat.CombatAbility.Trample);

		addEnterPlayEffect(null, "When {~} enters play, create a 3/3 green and white Wolf.",
				() -> {
					UnitToken.Wolf_3_3(game, controller).resolve();
				});

		triggering = new Triggering(game, this).addEventChecker(
				event -> {
					if (event.type == Event.EventType.Enter_Play && ((Card)event.data.get(0)).hasSubtype(CardSubtype.Wolf)){
						game.addToStack(new AbilityCard(game, this, "Whenever a Wolf enters play under your control, you gain 3 life and that unit fights up to one target unit you don’t control.",
								() -> {
										game.gainHealth(controller, 3);
										ArrayList<UUID> maybeTarget = game.selectFromZone(controller, Game.Zone.Opponent_Play, Predicates::isUnit, 1, true, "Choose a unit to fight.");
										if (!maybeTarget.isEmpty()){
												game.fight(id, maybeTarget.get(0));
										}
						}));
					}
				});
	}
}
