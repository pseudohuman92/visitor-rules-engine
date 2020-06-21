/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.card.properties.Combat.CombatAbility.Haste;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class StarlingSwarm extends Unit {

	public StarlingSwarm (Game game, String owner) {
		super(game, "Starling Swarm",
				6, new CounterMap(RED, 3),
				"",
				4, 4,
				owner, Flying, Haste);
	}
}
