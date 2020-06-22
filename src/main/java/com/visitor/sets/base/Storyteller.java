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
public class Storyteller extends Unit {

	public Storyteller (Game game, String owner) {
		super(game, "Storyteller",
				5, new CounterMap(),
				"",
				4, 4,
				owner);
	}
}
