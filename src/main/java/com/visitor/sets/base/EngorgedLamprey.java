/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class EngorgedLamprey extends Unit {

	public EngorgedLamprey (Game game, String owner) {
		super(game, "Engorged Lamprey",
				3, new CounterMap(PURPLE, 1),
				"",
				2, 2,
				owner, Deathtouch);
	}
}
