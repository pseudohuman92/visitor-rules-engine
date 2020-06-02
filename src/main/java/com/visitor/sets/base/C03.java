/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Defender;

/**
 * @author pseudo
 */
public class C03 extends Unit {

	public C03 (Game game, String owner) {
		super(game, "Black Bear",
				3, new CounterMap(),
				"",
				3, 3,
				owner, Defender);
	}
}
