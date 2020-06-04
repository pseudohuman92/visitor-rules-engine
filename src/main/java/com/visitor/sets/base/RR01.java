package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.Damage;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.game.Game.Zone.Play;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class RR01 extends Ritual {

	public RR01 (Game game, String owner) {
		super(game, "UR01", 1,
				new CounterMap<>(RED, 1),
				"Deal 3 damage to target unit with flying.",
				owner);

		playable
				.setCanPlayAdditional(() ->
						game.hasIn(playable.card.controller, Both_Play, and(Predicates::isUnit,c-> c.hasCombatAbility(Flying)), 1)
				)
				.setBeforePlay(() -> {
					targets.addAll(game.selectFromZone(playable.card.controller, Both_Play, and(Predicates::isUnit,c-> c.hasCombatAbility(Flying)), 1, false));
				})
				.setResolveEffect(() -> {
					if(game.isIn(controller, Both_Play, targets.get(0)))
						game.dealDamage(this.id, targets.get(0), new Damage(3));
				});
	}
}
