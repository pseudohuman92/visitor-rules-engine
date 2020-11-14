package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class BC06 extends Cantrip {
	public BC06 (Game game, String owner) {
		super(game, "BC06", 4,
				new CounterMap<>(BLUE, 2),
				"Cancel target unit card. Draw 1 card.",
				owner);

		playable
				.setTargetSingleUnit(Game.Zone.Stack, null, game::cancel, () -> game.draw(controller, 1));
	}
}
