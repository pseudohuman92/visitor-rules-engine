package com.visitor.sets.base;

import com.visitor.card.types.Cantrip;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.UUIDHelper;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class BC02 extends Cantrip {
	public BC02 (Game game, String owner) {
		super(game, "UR01", 2,
				new CounterMap<>(BLUE, 1),
				"Look at the top three cards of your library. Draw one of them and put the rest on the bottom of your library.",
				owner);

		// TODO: May generalize this.
		playable
				.setResolveEffect(() -> {
					Arraylist<Card> topCards = game.extractFromTopOfDeck(controller, 3);
					Arraylist<UUID> selected = game.selectFromList(controller, topCards, Predicates::any, 1, false, "");
					Arraylist<Card> selectedCards = UUIDHelper.getInList(topCards, selected);
					Arraylist<Card> toBottom = UUIDHelper.getNotInList(topCards, selected);
					game.putToBottomOfDeck(controller, toBottom);
					game.draw(controller, selectedCards.get(0));
				});
	}
}
