package com.visitor.sets.base;

import com.visitor.card.properties.Combat;
import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.Damage;

import static com.visitor.protocol.Types.Knowledge.RED;

public class RC04 extends Cantrip {
	public RC04 (Game game, String owner) {
		super(game, "UR01", 4,
				new CounterMap<>(RED, 2),
				"Deal 4 damage to target unit and 2 damage to its controller.",
				owner);

		playable
				.setTargetingSingleUnit((cardId) -> {
								game.dealDamage(id, cardId, new Damage(4));
								game.dealDamage(id, game.getUserId(game.getCard(cardId).controller), new Damage(2));
						}
				);
	}
}
