package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class BC03 extends Cantrip {
	public BC03 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(BLUE, 1),
				"Cancel target unit card.",
				owner);

		playable
				.setTargetSingleCardInStack(Predicates::isUnit, game::cancel
				);
	}
}
