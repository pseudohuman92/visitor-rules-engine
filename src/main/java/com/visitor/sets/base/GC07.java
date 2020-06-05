package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.protocol.Types.Knowledge.GREEN;

public class GC07 extends Cantrip {
	public GC07 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(GREEN, 2),
				"Draw the top unit from your deck.",
				owner);

		playable
				.setResolveEffect(() ->
					game.draw(controller, game.extractTopmostMatchingFromDeck(controller, Predicates::isUnit))
				);
	}
}
