/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class Yak extends Unit {

	public Yak (Game game, String owner) {
		super(game, "Yak",
				4, new CounterMap(GREEN, 1),
				"",
				5, 5,
				owner);
	}
}
