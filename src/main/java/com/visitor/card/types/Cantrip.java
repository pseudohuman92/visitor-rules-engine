package com.visitor.card.types;

import com.visitor.card.Card;
import com.visitor.card.properties.Playable;
import com.visitor.card.properties.Studiable;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types.Knowledge;


/**
 * Abstract class for the Spell card type.
 *
 * @author pseudo
 */
public abstract class Cantrip extends Spell {

	public Cantrip (Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, String owner) {
		super(game, name, cost, knowledge, text, owner);

		subtypes.add(CardSubtype.Cantrip);
		playable.setFast().setEphemeral();
	}
}
