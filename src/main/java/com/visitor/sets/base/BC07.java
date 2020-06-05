package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Stack;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class BC07 extends Cantrip {
	public BC07 (Game game, String owner) {
		super(game, "UR01", 5,
				new CounterMap<>(BLUE, 1),
				"Put target card on top of controller's deck.",
				owner);

		playable
				.setTargetingSingleUnitInBothPlay(game::putToTopOfDeck);
	}
}
