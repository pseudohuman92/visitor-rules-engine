/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.test;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;

import java.util.UUID;

/**
 * @author pseudo
 */
public class ActivatedAbility2 extends Unit {

	public ActivatedAbility2 (Game game, UUID owner) {
		super(game, "Activated Ability 2",
				0, new CounterMap(),
				"0: Gain +0/+1\n1: Gain +1/+0",
				0, 1,
				owner);
		activatable.addActivatedAbility(new ActivatedAbility(game, this, 0, "Gain +0/+1", ()->game.addAttackAndHealth(id, 0, 1)));
		activatable.addActivatedAbility(new ActivatedAbility(game, this, 1, "Gain +1/+0", ()->game.addAttackAndHealth(id, 1, 0)));
	}
}
