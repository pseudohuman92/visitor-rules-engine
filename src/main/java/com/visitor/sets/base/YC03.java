package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.helpers.Predicates.isUnit;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

public class YC03 extends Cantrip {
	public YC03 (Game game, String owner) {
		super(game,
				"YC03",
				4,
				new CounterMap<>(YELLOW, 3),
				"Units you control get +1/+2",
				owner);

		playable
				.setResolveEffect(() ->
						game.forEachInZone(controller, Game.Zone.Both_Play, card -> {
							if (isUnit(card)) {
								card.addAttack(1);
								card.addHealth(2);
							}
						}));
	}
}
