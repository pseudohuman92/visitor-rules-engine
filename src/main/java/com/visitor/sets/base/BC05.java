package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class BC05 extends Cantrip {
	public BC05 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(BLUE, 1),
				"Target unit gets -2/-0.",
				owner);

		playable
				.setTargetingSingleUnitInBothPlay((cardId)-> {
								game.getCard(targets.get(0)).addAttack(-2);
						}
				);
	}
}
