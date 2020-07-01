/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.test;

import com.visitor.card.types.Unit;
import com.visitor.card.types.specialtypes.ActivatableUnit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;

/**
 * @author pseudo
 */
public class ActivatedAbility1 extends ActivatableUnit {

	public ActivatedAbility1 (Game game, String owner) {
		super(game, "Activated Ability 1",
				0, new CounterMap(),
				"0: Gain +0/+1",
				0, 1,
				owner);
		activatable.addActivatedAbility(new ActivatedAbility(game, this, 0, "Gain +0/+1", ()->game.gainHealth(id, 1)));
	}
}
