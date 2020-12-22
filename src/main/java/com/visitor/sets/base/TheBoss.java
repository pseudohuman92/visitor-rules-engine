/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;


import com.visitor.card.types.Ally;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;

import static com.visitor.game.Game.Zone.Hand;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class TheBoss extends Ally {

    public TheBoss(Game game, String owner) {
        super(game, "The Boss", 2, new CounterMap<>(PURPLE, 1),
                "{S} | Discard a card, {D}:    \n" +
                "    +1 {Loyalty}. Opponent discards a card.\n" +
                "\n" +
                "{Condition} - Opponent has no cards in hand\n" +
                "  {S} | -2 {Loyalty}, {D}: \n" +
                "       {Delay} 1 - Deal 2 damage",
                1,
                owner);

        addPlusLoyaltyAbility(0 , "Discard a card, {D}:\n\t+1 {Loyalty}. Opponent discards a card.", 1,
		        () -> game.discard(game.getOpponentName(controller), 1),
		        () -> game.hasIn(controller, Hand, Predicates::any, 1),
		        () -> game.discard(controller, 1));

        addMinusLoyaltyAbility(0,"-2 {Loyalty}, {D}:\n\t{Delay} 1 - Deal 2 damage", 2, 1,
				        new ActivatedAbility(game, this, 0, "Deal 2 Damage")
							        .setTargetingForDamage(targetID -> game.dealDamage(id, targetID, 2)),
				        ()-> !game.hasIn(game.getOpponentName(controller), Hand, Predicates::any, 1),
		            null);

    }

}

