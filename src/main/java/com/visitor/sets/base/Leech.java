/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Lifelink;
import static com.visitor.protocol.Types.Knowledge.BLUE;

/**
 * @author pseudo
 */
public class Leech extends Unit {

	public Leech (Game game, String owner) {
		super(game, "Leech",
				1, new CounterMap(BLUE, 1),
				"",
				1, 1,
				owner, Lifelink);
	}
}
