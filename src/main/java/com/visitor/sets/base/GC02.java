package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.game.Game.Zone.Play;
import static com.visitor.protocol.Types.Knowledge.GREEN;

public class GC02 extends Cantrip {
	public GC02 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(GREEN, 1),
				"Target unit heals 3.",
				owner);

		playable
				.setTargetingSingleUnitInBothPlay((cardId) -> {
								game.getCard(cardId).heal(3);
							}
				);
	}
}
