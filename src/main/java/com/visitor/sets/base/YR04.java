package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YR04 extends Ritual {

	public YR04 (Game game, String owner) {
		super(game, "UR01", 3,
				new CounterMap<>(YELLOW, 1),
				"Destroy target depleted unit. Gain 3 health",
				owner);

		playable
				.setCanPlayAdditional(() ->
						game.hasIn(controller, Both_Play, and(Predicates::isUnit, c-> c.depleted), 1)
				)
				.setBeforePlay(() -> {
					targets.addAll(game.selectFromZone(controller, Both_Play, and(Predicates::isUnit, c-> c.depleted), 1, false));
				})
				.setResolveEffect(() -> {
					if (game.isIn(controller, Both_Play, targets.get(0)))
							game.destroy(id, targets.get(0));
							game.gainHealth(controller, 3);
					});
	}
}
