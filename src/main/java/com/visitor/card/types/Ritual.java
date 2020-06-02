package com.visitor.card.types;

import com.visitor.card.Card;
import com.visitor.card.properties.Playable;
import com.visitor.card.properties.Studiable;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types.Knowledge;


/**
 * Abstract class for the Spell card type.
 *
 * @author pseudo
 */
public abstract class Ritual extends Card {

	public Ritual (Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, String owner) {
		super(game, name, knowledge, CardType.Ritual, text, owner);

		playable = new Playable(game, this, cost).setSlow().setEphemeral();
		studiable = new Studiable(game, this);
	}
}
