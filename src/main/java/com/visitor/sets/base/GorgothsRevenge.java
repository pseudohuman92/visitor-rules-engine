/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.YELLOW;

/**
 * @author pseudo
 */
public class GorgothsRevenge extends Unit {

	public GorgothsRevenge (Game game, String owner) {
		super(game, "Gorgoth's Revenge",
				2, new CounterMap(YELLOW, 1),
				"",
				3, 1,
				owner);
	}
}
