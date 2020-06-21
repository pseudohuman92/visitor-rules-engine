/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Trample;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class PorciniatheGreat extends Unit {

	public PorciniatheGreat (Game game, String owner) {
		super(game, "Porcinia the Great",
				1, new CounterMap(RED, 1),
				"",
				1, 1,
				owner, Trample);
	}
}
