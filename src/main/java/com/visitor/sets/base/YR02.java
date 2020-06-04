package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YR02 extends Ritual {

	public YR02 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(YELLOW, 1),
				"Put target unit with attack >= 4 to bottom of its controller's deck.",
				owner);

		playable
				.setCanPlayAdditional(() ->
						game.hasIn(controller, Both_Play, and(Predicates::isUnit, c-> c.getAttack() >= 4), 1)
				)
				.setBeforePlay(() -> {
					targets.addAll(game.selectFromZone(controller, Both_Play, and(Predicates::isUnit, c-> c.getAttack() >= 4), 1, false));
				})
				.setResolveEffect(() -> {
					if (game.isIn(controller, Both_Play, targets.get(0)))
						game.putToBottomOfDeck(targets.get(0));
				});
	}
}
