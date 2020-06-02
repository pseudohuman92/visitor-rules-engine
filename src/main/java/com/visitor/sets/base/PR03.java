package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.game.Game.Zone.Discard_Pile;
import static com.visitor.game.Game.Zone.Opponent_Hand;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class PR03 extends Ritual {

	public PR03 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(PURPLE, 1),
				"Target opponent reveals their hand. You choose a unit card from it. \n" +
						"That player discards that card.",
				owner);

		playable
				.setBeforePlay(() -> {
					targets.add(game.selectFromZone(playable.card.controller, Discard_Pile, Predicates::isUnit, 1, false).get(0));
				})
				.setResolveEffect(() -> {
					boolean hasUnit = game.hasIn(playable.card.controller, Opponent_Hand, Predicates::isUnit, 1);
					Arraylist<UUID> discardedCard = game.selectFromZone(playable.card.controller, Opponent_Hand, Predicates::isUnit, 1, !hasUnit);
					if (discardedCard.size() > 0) {
						game.discard(game.getOpponentName(playable.card.controller), discardedCard.get(0));
					}
				});
	}
}
