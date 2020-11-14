package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.Damage;

import static com.visitor.protocol.Types.Knowledge.RED;

public class RC03 extends Cantrip {
	public RC03 (Game game, String owner) {
		super(game, "RC03", 3,
				new CounterMap<>(RED, 1),
				"Deal 2 damage to each unit.",
				owner);

		playable
				.setResolveEffect(() ->
						game.forEachInZone(controller, Game.Zone.Both_Play,
								card -> game.dealDamage(id, card.id, new Damage(2))));
	}
}
