package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class BC01 extends Cantrip {
	public BC01 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(BLUE, 1),
				"Return target unit to its controller's hands.",
				owner);

		playable
				.setTargetingSingleUnitInBothPlay((cardId) -> {
								game.getCard(cardId).returnToHand();
						}
				);
	}
}
