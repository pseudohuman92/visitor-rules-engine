/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.test;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;

/**
 * @author pseudo
 */
public class Deathtouchy extends Unit {

	public Deathtouchy (Game game, String owner) {
		super(game, "Deathtouchy",
				0, new CounterMap(),
				"",
				1, 1,
				owner, Deathtouch);
	}
}
