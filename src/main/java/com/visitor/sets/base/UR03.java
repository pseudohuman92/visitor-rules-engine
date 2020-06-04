package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.card.properties.Combat.CombatAbility.Unblockable;
import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class UR03 extends Ritual {
	public UR03 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(BLUE, 1),
				"Up to 2 target units gains unblockable until end of turn",
				owner);

		playable
				.setCanPlayAdditional(() ->
						game.hasIn(playable.card.controller, Both_Play, Predicates::isUnit, 1)
				)
				.setBeforePlay(() ->
						targets = game.selectFromZone(playable.card.controller, Both_Play, Predicates::isUnit, 2, true)
				)
				.setResolveEffect(() -> {
					targets.forEach(cardId -> {
						if (game.isIn(controller, Both_Play, cardId))
							game.getCard(cardId).addTurnlyCombatAbility(Unblockable);
					});
				});
	}
}
