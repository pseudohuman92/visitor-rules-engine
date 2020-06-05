package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class PR06 extends Ritual {

	public PR06 (Game game, String owner) {
		super(game, "UR01", 3,
				new CounterMap<>(PURPLE, 1),
				"Target player discards 2 cards",
				owner);

		playable
				.setSinglePlayerTargetingResolve(playerId -> game.discard(game.getUsername(playerId), 2));
	}
}
