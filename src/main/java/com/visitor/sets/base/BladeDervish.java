/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Haste;
import static com.visitor.helpers.Predicates.isUnit;
import static com.visitor.helpers.Predicates.none;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class BladeDervish extends Unit {

	public BladeDervish (Game game, String owner) {
		super(game, "Blade Dervish",
				4, new CounterMap(RED, 1),
				"When {~} enters play, it deals 3 damage to target unit.",
				3, 3,
				owner, Haste);

		playable.addEnterPlayEffect("When {~} enters play, it deals 3 damage to target unit.",
				()-> {
						UUID damageTarget = game.selectDamageTargetsConditional(controller, Predicates::isUnit, Predicates::none, 1, false, "Choose a unit to damage").get(1);
						game.addToStack(new AbilityCard(game, this, "Deal 3 damage.",
								()-> game.dealDamage(id, damageTarget, 3), damageTarget));
					}
				);
	}
}
