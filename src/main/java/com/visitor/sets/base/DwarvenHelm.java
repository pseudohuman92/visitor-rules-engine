/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class DwarvenHelm extends Unit {

	public DwarvenHelm (Game game, String owner) {
		super(game, "Dwarven Helm",
				3, new CounterMap(RED, 1),
				"",
				3, 2,
				owner);
	}
}
