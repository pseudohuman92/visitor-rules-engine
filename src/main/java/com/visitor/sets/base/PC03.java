package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.Damage;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class PC03 extends Cantrip {
	public PC03 (Game game, String owner) {
		super(game, "PC03", 2,
				new CounterMap<>(PURPLE, 2),
				"Deal 2 damage to target unit. You gain 2 life.",
				owner);

		playable
				.setTargetSingleUnit(null, null,
						cardId -> game.dealDamage(id, cardId, new Damage(2)),
						() -> game.gainHealth(controller, 2)
				);
	}
}
