/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;


import com.visitor.card.types.Ally;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;


import static com.visitor.game.Game.Zone.Hand;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class TheHighwayman extends Ally {

    public TheHighwayman(Game game, String owner) {
        super(game,"The Highwayman", 2, new CounterMap<>(PURPLE, 2),
                "{S} | {3}, {D}: \n" +
				                "  +2 {Loyalty}\n" +
				                "\n" +
				                "{S} | -1 {Loyalty}, {D}:\n" +
				                "  {Delay} 2 - Draw the top Unit of opponent's deck.", 5,
                owner);

	    addPlusLoyaltyAbility(3, "{3}, {D}: +2 {Loyalty}", 2, null, null, null);

			addMinusLoyaltyAbility(0,"-1 {Loyalty}, {D}:\n\t{Delay} 2 - Draw the top Unit of opponent's deck.", 1, 2,
					    new ActivatedAbility(game, this, 0, "Draw the top unit of opponent's deck.",
									    () -> {
										    Card c = game.extractTopmostMatchingFromDeck(game.getOpponentName(controller), Predicates::isUnit);
										    if (c != null) {
											    c.controller = controller;
											    c.knowledge = new CounterMap<>();
											    game.putTo(c.controller, c, Hand);
										    }
								    }), null, null);
    }

}

