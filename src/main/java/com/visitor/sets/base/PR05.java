package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class PR05 extends Ritual {

	public PR05 (Game game, String owner) {
		super(game, "UR01", 3,
				new CounterMap<>(PURPLE, 1),
				"Destroy target unit.",
				owner);

		playable
				.setCanPlayAdditional(() ->
						game.hasIn(playable.card.controller, Both_Play, Predicates::isUnit, 1)
				)
				.setBeforePlay(() -> {
					targets.addAll(game.selectFromZone(controller, Both_Play, Predicates::isUnit, 1, false));
				})
				.setResolveEffect(() ->
						HelperFunctions.runIfNotNull(game.getCard(targets.get(0)),
								() -> game.destroy(id, targets.get(0)))
				);
	}
}
