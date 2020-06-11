package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.Damage;

import static com.visitor.protocol.Types.Knowledge.RED;

public class RR04 extends Ritual {

	public RR04 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(RED, 1),
				"Deal 3 damage to a target.",
				owner);

		playable
				.setTargetSingleUnitOrPlayer(targetId ->
						game.dealDamage(id, targetId, new Damage(5))
				);
	}
}
