/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Haste;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class R08 extends Unit {

	public R08 (Game game, String owner) {
		super(game, "Black Bear",
				2, new CounterMap(RED, 2),
				"",
				3, 1,
				owner, Haste);
	}
}
