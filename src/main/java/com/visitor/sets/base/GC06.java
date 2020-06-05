package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Deck;
import static com.visitor.protocol.Types.Knowledge.GREEN;

public class GC06 extends Cantrip {
	public GC06 (Game game, String owner) {
		super(game, "UR01", 5,
				new CounterMap<>(GREEN, 3),
				"Choose and draw up to 2 units from your deck.",
				owner);

		playable
				.setTargetingResolveFromZone(Deck, Predicates::isUnit, 2, true,
						cardId -> {
					game.draw(controller, cardId);
				}, ()-> game.shuffleDeck(controller));
	}
}
