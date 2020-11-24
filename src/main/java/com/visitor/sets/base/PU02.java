/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.properties.Combat;
import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class PU02 extends Unit {

	public PU02 (Game game, String owner) {
		super(game, "PU02",
				6, new CounterMap(BLUE, 2).add(PURPLE, 1),
				"Whenever {~} deals combat damage to a player, you may play target \n" +
				"spell from that player’s discard pile without paying its cost. \n" +
				"Purge the spell after it resolves.",
				5, 8,
				owner);

		combat.addDamageEffect(
				(targetId, damage) -> {
					if (game.isPlayer(targetId)) {
						game.addToStack(new AbilityCard(game, this, "Whenever {~} deals combat damage to a player, you may play target \n" +
						                                            "spell from that player’s discard pile without paying its cost. \n" +
						                                            "Purge the spell after it resolves.",
								() -> {
									Arraylist<UUID> maybePlay = game.selectFromZone(controller, Game.Zone.Opponent_Discard_Pile, Predicates::isSpell, 1, true, "Select a spell to play.");
									if (!maybePlay.isEmpty()){
										game.getCard(maybePlay.get(0)).controller = controller;
										game.getCard(maybePlay.get(0)).setPurging();
										game.playCardWithoutCost(controller, maybePlay.get(0));
									}
								}));

					}

		}
		);
	}
}
