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
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class DungBeetle extends Unit {

	public DungBeetle (Game game, String owner) {
		super(game, "Dung Beetle",
				1, new CounterMap(GREEN, 1),
				"",
				1, 1,
				owner, Trample);
	}
}
