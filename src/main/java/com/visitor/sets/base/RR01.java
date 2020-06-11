package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.Damage;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.helpers.Predicates.or;
import static com.visitor.protocol.Types.Knowledge.RED;

public class RR01 extends Ritual {

	public RR01 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(RED, 1),
				"Deal 3 damage to target unit with flying.",
				owner);

		playable
				.setTargetSingleUnit(c -> c.hasCombatAbility(Flying), cardId -> game.dealDamage(this.id, cardId, new Damage(3)));
	}
}
