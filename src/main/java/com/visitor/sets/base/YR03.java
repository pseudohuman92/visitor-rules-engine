package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YR03 extends Ritual {

	public YR03 (Game game, String owner) {
		super(game, "YR03", 2,
				new CounterMap<>(YELLOW, 1),
				"Destroy target depleted unit",
				owner);

		playable
				.setTargetSingleUnit(null, Card::isDepleted, cardId -> game.destroy(id, cardId), null);
	}
}
