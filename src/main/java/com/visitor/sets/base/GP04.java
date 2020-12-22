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
import com.visitor.card.types.helpers.EventChecker;
import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.sets.token.UnitToken;

import static com.visitor.protocol.Types.Knowledge.GREEN;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class GP04 extends Unit {

	public GP04 (Game game, String owner) {
		super(game, "GP04",
				3, new CounterMap(GREEN, 1).add(PURPLE, 1),
				"At the end of your turn, each player creates a 1/1 green Plant.\n" +
						 "Whenever a unit dies, {~} gains +1/+1.",
				3, 3,
				owner, Combat.CombatAbility.Trample);

		triggering.addEventChecker(new EventChecker(game, this,
				event -> {
					UnitToken.Plant_1_1(game, controller).resolve();
					UnitToken.Plant_1_1(game, game.getOpponentName(controller)).resolve();
				})
				.addEndOfControllerTurnChecker()
				.createAbility("At the end of your turn, each player creates a 1/1 green Plant."));

			triggering.addEventChecker(new EventChecker(game, this,
					event -> game.addAttackAndHealth(id, 1, 1))
					.addTypeChecker(Event.EventType.Destroy)
					.createAbility("Whenever a unit dies, {~} gains +1/+1."));

				triggering.addEventChecker(new EventChecker(game, this,
						event -> game.addAttackAndHealth(id, 1, 1))
				.addTypeChecker(Event.EventType.Sacrifice)
				.createAbility("Whenever a unit dies, {~} gains +1/+1."));
	}
}
