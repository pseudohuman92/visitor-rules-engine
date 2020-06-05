package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.game.Game.Zone.Play;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.GREEN;

public class GC01 extends Cantrip {
	public GC01 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(GREEN, 1),
				"Target unit gets +1/+1 until end of turn for each unit you control.",
				owner);

		playable
				.setTargetingSingleUnitInBothPlay((cardId) -> {
								int count = game.countInZone(controller, Play, Predicates::isUnit);
								game.getCard(cardId).addTurnlyAttack(count);
								game.getCard(cardId).addTurnlyHealth(count);
							}

				);
	}
}
