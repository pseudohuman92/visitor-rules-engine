/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.card.properties.Combat.CombatAbility.Haste;
import static com.visitor.protocol.Types.Knowledge.*;

/**
 * @author pseudo
 */
public class GU01 extends Unit {

	public GU01 (Game game, String owner) {
		super(game, "GU01",
				5, new CounterMap(BLUE, 1).add(GREEN, 1),
				"Whenever you play a green unit, search your library for a unit, shuffle your library and put that card on top of it.\n" +
						"Whenever you play a blue unit, Draw the top card of your deck if it's a unit.",
				2, 2,
				owner);

		triggering = new Triggering(game, this)
				.addEventChecker(event -> {
					if (event.type == Event.EventType.Play_Card){
						Card playedCard = ((Card)event.data.get(0));
						if (playedCard.controller.equals(controller) &&
								playedCard.hasType(CardType.Unit)){

							if (playedCard.hasColor(GREEN)){
								game.addToStack(new AbilityCard(game, this, "Whenever you play a green unit, search your library for a unit, shuffle your library and put that card on top of it.",
										() -> {
											Arraylist<UUID> selectedUnit = game.selectFromZone(controller,
													Game.Zone.Deck,
													Predicates::isUnit,
													1,
													!game.hasIn(controller, Game.Zone.Deck, Predicates::isUnit, 1),
													"Choose a unit from your deck.");
											if (!selectedUnit.isEmpty()){
												game.shuffleDeck(controller);
												game.putToTopOfDeck(selectedUnit.get(0));
											}
										}
										));
							}
							if (playedCard.hasColor(BLUE)) {
								game.addToStack(new AbilityCard(game, this, "Whenever you play a blue unit, Draw the top card of your deck if it's a unit.",
										() -> {
											if (game.getTopCardsFromDeck(controller, 1).get(0).hasType(CardType.Unit)) {
												game.draw(controller, 1);
											}
										}));
							}
						}
					}
		});
	}
}
