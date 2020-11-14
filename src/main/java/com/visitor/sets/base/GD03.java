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
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.sets.token.UnitToken;

import static com.visitor.protocol.Types.Knowledge.GREEN;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class GD03 extends Unit {

	public GD03 (Game game, String owner) {
		super(game, "GD03",
				4, new CounterMap(GREEN, 1).add(RED, 1),
						"Whenever {~} deals combat damage to a player, create that many 1/1 green Insect",
				2, 2,
				owner, Combat.CombatAbility.Trample);

		combat.addDamageEffect((targetId, damage) -> {
			game.addToStack(new AbilityCard(game, this, "Whenever {~} deals combat damage to a player, create that many 1/1 green Insect",
					() -> {
						if (game.isPlayer(targetId) && damage.combat && damage.amount > 0) {
							for (int i = 0; i < damage.amount; i++) {
								UnitToken.Insect_1_1(game, controller).resolve();
							}
						}
					}));
		});
	}
}
