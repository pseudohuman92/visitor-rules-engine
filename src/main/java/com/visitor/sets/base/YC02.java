package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YC02 extends Cantrip {
	public YC02 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(YELLOW, 1),
				"Target unit gets +2/+4 until end of turn.",
				owner);

		playable
				.setTargetSingleUnit((cardId) -> {
							game.getCard(cardId).addTurnlyAttack(2);
							game.getCard(cardId).addTurnlyHealth(4);
						}
				);
	}
}
