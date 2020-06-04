package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.Damage;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.RED;

public class RR04 extends Ritual {

	public RR04 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(RED, 1),
				"Deal 3 damage to a target.",
				owner);

		playable
				.setBeforePlay(() -> {
					targets.addAll(game.selectFromZoneWithPlayers(playable.card.controller, Both_Play, Predicates::isUnit, Predicates::any, 1, false));
				})
				.setResolveEffect(() -> {
					if (game.isIn(controller, Both_Play, targets.get(0)))
						game.dealDamage(this.id, targets.get(0), new Damage(5));
				});
	}
}
