package com.visitor.sets.base;

import com.visitor.card.properties.Combat;
import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YC01 extends Cantrip {
	public YC01 (Game game, String owner) {
		super(game, "YC01", 2,
				new CounterMap<>(YELLOW, 1),
				"Target unit gets +2/+2 and lifelink until end of turn.",
				owner);

		playable
				.setTargetSingleUnit(null, null, cardId -> {
							game.getCard(cardId).addTurnlyAttack(2);
							game.getCard(cardId).addTurnlyHealth(2);
							game.getCard(cardId).addTurnlyCombatAbility(Combat.CombatAbility.Lifelink);
						}, null);
	}
}
