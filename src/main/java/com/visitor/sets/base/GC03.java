package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.GREEN;

public class GC03 extends Cantrip {
	public GC03 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(GREEN, 1),
				"Target unit gets +1/+2",
				owner);

		playable
				.setTargetingSingleUnitInBothPlay((cardId) -> {
							game.getCard(cardId).addAttack(1);
							game.getCard(cardId).addHealth(2);

						}
				);
	}
}
