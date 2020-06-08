package com.visitor.card.types;

import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types.Knowledge;


/**
 * Abstract class for the Spell card type.
 *
 * @author pseudo
 */
public abstract class Ritual extends Spell {

	public Ritual (Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, String owner) {
		super(game, name, cost, knowledge, text, owner);

		subtypes.add(CardSubtype.Ritual);
		playable.setSlow().setEphemeral();
	}
}
