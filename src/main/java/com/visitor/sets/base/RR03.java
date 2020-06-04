package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.Damage;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.RED;

public class RR03 extends Ritual {

	public RR03 (Game game, String owner) {
		super(game, "UR01", 5,
				new CounterMap<>(RED, 1),
				"Deal 5 damage to target unit.",
				owner);

		playable
				.setCanPlayAdditional(() ->
						game.hasIn(playable.card.controller, Both_Play, Predicates::isUnit, 1)
				)
				.setBeforePlay(() -> {
					targets.addAll(game.selectFromZone(playable.card.controller, Both_Play, Predicates::isUnit, 1, false));
				})
				.setResolveEffect(() -> {
					if (game.isIn(controller, Both_Play, targets.get(0)))
						game.dealDamage(this.id, targets.get(0), new Damage(5));
				});
	}
}
