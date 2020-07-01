/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.specialtypes.ActivatableUnit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.protocol.Types.Knowledge.BLUE;

/**
 * @author pseudo
 */
public class Eagle extends ActivatableUnit {

	int x;

	public Eagle (Game game, String owner) {
		super(game, "Eagle",
				5, new CounterMap(BLUE, 3),
				"{X}, {D}: {~} becomes X/X",
				4, 4,
				owner, Flying);

		ActivatedAbility ability = new ActivatedAbility(game, this, 0, "{~} becomes" + x + "/" + x,
				() -> game.runIfInZone(controller, Game.Zone.Both_Play, id,
						() -> game.setAttackAndHealth(id, x, x)))
				.setDepleting();
		ability.setBeforeActivate(() -> {
			x = game.selectX(controller, game.getPlayerEnergy(controller));
			ability.setCost(x);
		});
		activatable.addActivatedAbility(ability);
	}
}
