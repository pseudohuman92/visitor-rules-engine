/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.card.properties.Combat.CombatAbility.Unblockable;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class GU02 extends Unit {

	public GU02 (Game game, String owner) {
		super(game, "GU02",
				2, new CounterMap(BLUE, 1).add(GREEN, 1),
				"",
				2, 2,
				owner, Unblockable);
	}
}
