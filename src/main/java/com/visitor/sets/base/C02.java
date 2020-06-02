/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

/**
 * @author pseudo
 */
public class C02 extends Unit {

	public C02 (Game game, String owner) {
		super(game, "Black Bear",
				2, new CounterMap(),
				"",
				2, 1,
				owner);
	}
}
