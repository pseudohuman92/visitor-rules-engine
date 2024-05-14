/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.properties.Combat;
import com.visitor.card.types.Unit;
import com.visitor.card.types.helpers.Ability;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class PU01 extends Unit {

    public PU01(Game game, UUID owner) {
        super(game, "PU01",
                8, new CounterMap(BLUE, 2).add(PURPLE, 2),
                "Whenever {~} deals combat damage to a player,\n" +
                        "you may play a card from that player's hand without paying its cost.",
                6, 6,
                owner, Combat.CombatAbility.Trample);

        combat.addDamageEffect(
                (targetId, damage) -> {
                    if (game.isPlayer(targetId)) {
                        game.addToStack(new Ability(game, this, "Whenever {~} deals combat damage to a player,\n" +
                                "you may play a card from that player's hand without paying its cost.",
                                () -> {
                                    Arraylist<UUID> maybePlay = game.selectFromZone(controller, Game.Zone.Opponent_Hand, Predicates::any, 1, true, "Select a card to play.");
                                    if (!maybePlay.isEmpty()) {
                                        game.getCard(maybePlay.get(0)).controller = controller;
                                        game.playCardWithoutCost(controller, maybePlay.get(0));
                                    }
                                }));

                    }

                }
        );
    }
}
