package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class PR05 extends Ritual {

	public PR05 (Game game, String owner) {
		super(game, "PR05", 3,
				new CounterMap<>(PURPLE, 1),
				"Destroy target unit.",
				owner);

		playable
				.setTargetSingleUnit(null, null, cardId -> game.destroy(id, cardId), null);
	}
}
