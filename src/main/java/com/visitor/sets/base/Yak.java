/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.card.types.specialtypes.ActivatableUnit;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;

import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class Yak extends ActivatableUnit {

	public Yak (Game game, String owner) {
		super(game, "Yak",
				4, new CounterMap(GREEN, 1),
				"{S} | {D}, Sacrifice {~}: Draw X cards where X is equal to greatest attack among the units you control.",
				5, 5,
				owner);

		activatable
				.addActivatedAbility(new ActivatedAbility(game, this, 0, "Draw X cards where X is equal to greatest attack among the units you control.",
						() -> {
							final Integer[] i = {0};
							game.forEachInZone(controller, Game.Zone.Play, Predicates::isUnit, (cardId) -> {
								int attack = game.getAttack(cardId);
								if (attack > i[0]){
									i[0] = attack;
								}
							});
							game.draw(controller,i[0]);
						})
				.setSlow()
				.setDepleting()
				.setSacrificing());
	}
}
