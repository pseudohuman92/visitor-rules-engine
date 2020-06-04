package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Discard_Pile;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class PR04 extends Ritual {

	public PR04 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(PURPLE, 1),
				"Restore and return up to 2 target unit cards from your discard pile to your hand, \n" +
						"then discard 1 card.",
				owner);

		playable
				.setBeforePlay(() -> {
					targets.addAll(game.selectFromZone(controller, Discard_Pile, Predicates::isUnit, 2, true));
				})
				.setResolveEffect(() -> {
							targets.forEach(targetId -> {
								if (game.isIn(controller, Discard_Pile, targetId)) {
									game.restore(targetId).returnToHand();
								}
							});
							game.discard(controller, 1);
						}
				);
	}
}
