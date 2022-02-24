/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.test;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Haste;

/**
 * @author pseudo
 */
public class Hasty extends Unit {

	public Hasty (Game game, UUID owner) {
		super(game, "Hasty",
				0, new CounterMap(),
				"",
				1, 1,
				owner, Haste);
	}
}
