/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.card.properties.Combat.CombatAbility.Trample;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class GU03 extends Unit {

	public GU03 (Game game, String owner) {
		super(game, "GU03",
				2, new CounterMap(BLUE, 1).add(GREEN, 1),
				"Whenever {~} deals damage to an opponent, draw a card.",
				1, 1,
				owner);

		combat.addDamageEffect((targetId, damage) -> {
				if (game.isPlayer(targetId) && !controller.equals(game.getUsername(targetId))){
					game.addToStack(new AbilityCard(game, this, "Whenever {~} deals damage to an opponent, draw a card.",
							() -> game.draw(controller, 1)));
				}
		});
	}
}
