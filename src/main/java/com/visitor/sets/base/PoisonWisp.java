/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.Ability;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class PoisonWisp extends Unit {

	public PoisonWisp (Game game, String owner) {
		super(game, "Poison Wisp",
				6, new CounterMap(PURPLE, 1),
				"When {~} enters play, you may destroy target unit.",
				5, 5,
				owner, Deathtouch);

		playable.addEnterPlayEffect("When {~} enters play, you may destroy target unit.",
						() -> {
							Arraylist<UUID> destroyedIds = game.selectFromZone(controller, Game.Zone.Both_Play, Predicates::isUnit, 1, true, "You may destroy a unit.");
							if (destroyedIds.size() > 0) {
								UUID destroyedId = destroyedIds.get(0);
								game.destroy(id, destroyedId);
							}
						});
	}
}
