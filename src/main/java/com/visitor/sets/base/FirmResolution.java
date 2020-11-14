package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Unblockable;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class FirmResolution extends Ritual {
	public FirmResolution (Game game, String owner) {
		super(game, "Firm Resolution", 2,
				new CounterMap<>(BLUE, 1),
				"Up to 2 target units gains unblockable until end of turn",
				owner);

		playable
				.setTargetMultipleUnits(null, 2, true, cardId -> game.getCard(cardId).addTurnlyCombatAbility(Unblockable), null);
	}
}
