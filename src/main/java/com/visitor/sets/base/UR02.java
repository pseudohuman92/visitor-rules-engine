package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class UR02 extends Ritual {
	public UR02 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(BLUE, 1),
				"Draw 2 cards",
				owner);

		playable
				.setResolveEffect(() ->
						game.draw(controller, 2)
				);
	}
}
