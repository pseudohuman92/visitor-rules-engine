package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.helpers.Predicates.or;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YR01 extends Ritual {

	public YR01 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(YELLOW, 1),
				"Destroy target purple or red unit.",
				owner);

		playable
				.setTargetSingleUnit(or(Predicates::isPurple, Predicates::isRed), cardId -> game.destroy(id, cardId));
	}
}
