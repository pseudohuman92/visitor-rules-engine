package com.visitor.sets.test;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.*;
import static com.visitor.protocol.Types.Knowledge.*;

/**
 * Generated by card-lang
 */
public class TestsName extends Unit {

	public TestsName (Game game, UUID owner) {
		super(game, "Test's, Name", 2,
				new CounterMap().add(GREEN, 2).add(PURPLE, 1).add(RED, 1).add(BLUE, 1).add(YELLOW, 3),
				"this is card text",
				2, 2,
				owner, Flying, Deathtouch, Unblockable);
	}
}
