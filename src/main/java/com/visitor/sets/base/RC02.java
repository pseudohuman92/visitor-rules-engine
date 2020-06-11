package com.visitor.sets.base;

import com.visitor.card.properties.Combat;
import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.RED;

public class RC02 extends Cantrip {
	public RC02 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(RED, 1),
				"Target unit gets +3/+0 and gains first strike until end of turn.",
				owner);

		playable
				.setTargetSingleUnit((cardId) -> {
							game.getCard(cardId).addTurnlyAttack(3);
							game.getCard(cardId).addTurnlyCombatAbility(Combat.CombatAbility.FirstStrike);
						}
				);
	}
}
