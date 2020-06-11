package com.visitor.card.types;

import com.visitor.card.properties.Playable;
import com.visitor.card.properties.Studiable;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types.Knowledge;

/**
 * Abstract class for the Asset card type.
 *
 * @author pseudo
 */
public abstract class Asset extends Card {

	public Asset (Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, String owner) {
		super(game, name, knowledge, CardType.Asset, text, owner);

		playable = new Playable(game, this, cost).setSlow().setPersistent();
		studiable = new Studiable(game, this);
	}
}
