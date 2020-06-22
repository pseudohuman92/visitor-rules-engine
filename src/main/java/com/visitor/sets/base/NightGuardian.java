/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Vigilance;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

/**
 * @author pseudo
 */
public class NightGuardian extends Unit {

	public NightGuardian (Game game, String owner) {
		super(game, "Night Guardian",
				5, new CounterMap(YELLOW, 3),
				"",
				4, 6,
				owner, Vigilance);
	}
}
