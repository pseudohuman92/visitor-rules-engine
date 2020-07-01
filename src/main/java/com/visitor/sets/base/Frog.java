/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.card.types.specialtypes.ActivatableUnit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.helpers.containers.Damage;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.card.properties.Combat.CombatAbility.Reach;
import static com.visitor.helpers.Predicates.and;
import static com.visitor.helpers.Predicates.not;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class Frog extends ActivatableUnit {

	UUID target;

	public Frog (Game game, String owner) {
		super(game, "Frog",
				1, new CounterMap(GREEN, 1),
				"{D}: Deal 1 damage to target unit with flying.",
				1, 2,
				owner, Reach);

		activatable
				.addActivatedAbility(new ActivatedAbility(game, this, 0, "Deal 1 damage to target unit with flying.",
						()->game.hasIn(controller, Game.Zone.Both_Play, and(Predicates::isUnit, c->c.hasCombatAbility(Flying)), 1),
						()-> target = game.selectFromZone(controller, Game.Zone.Both_Play, and(Predicates::isUnit, c->c.hasCombatAbility(Flying)), 1, false,"Select a flying unit.").get(0),
						()-> game.runIfInZone(controller, Game.Zone.Both_Play, target, ()->game.dealDamage(id, target, new Damage(1))))
						.setDepleting());

	}
}
