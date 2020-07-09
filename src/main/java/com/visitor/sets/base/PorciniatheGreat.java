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

import static com.visitor.card.properties.Combat.CombatAbility.Trample;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class PorciniatheGreat extends ActivatableUnit {

	public PorciniatheGreat (Game game, String owner) {
		super(game, "Porcinia the Great",
				1, new CounterMap(RED, 1),
				"{D}: Target opponent gains control of target unit you control.",
				1, 1,
				owner, Trample);

		activatable.addActivatedAbility(new ActivatedAbility(game, this, 0, "{D}: Target opponent gains control of target unit you control.")
				.setTargeting(Game.Zone.Play, Predicates::isUnit, 1, false,
						targetId -> game.gainControlFromZone(game.getOpponentName(controller), Game.Zone.Opponent_Play, Game.Zone.Play, targetId))
		.setDepleting());
	}
}
