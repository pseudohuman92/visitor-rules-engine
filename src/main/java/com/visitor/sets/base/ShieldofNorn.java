/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class ShieldofNorn extends Unit {

	public ShieldofNorn (Game game, String owner) {
		super(game, "Shield of Norn",
				4, new CounterMap(PURPLE, 2),
				"",
				4, 4,
				owner);
	}
}
