package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YR05 extends Ritual {

	public YR05 (Game game, String owner) {
		super(game, "UR01", 5,
				new CounterMap<>(YELLOW, 3),
				"Destroy all units.",
				owner);

		playable
				.setResolveEffect(game::destroyAllUnits);
	}
}
