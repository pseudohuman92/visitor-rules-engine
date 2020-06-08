package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class BC01 extends Cantrip {
	public BC01 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(BLUE, 1),
				"Return target unit to its controller's hands.",
				owner);

		playable
				.setTargetingSingleUnit((cardId) -> {
								game.getCard(cardId).returnToHand();
						}
				);
	}
}
