/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types;

import com.visitor.card.properties.Combat;
import com.visitor.card.properties.Playable;
import com.visitor.card.properties.Studiable;
import com.visitor.card.properties.Triggering;
import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.card.types.helpers.EventChecker;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

import static java.lang.Math.max;
import static java.lang.System.out;

/**
 * @author pseudo
 */
public abstract class Ally extends Card {


	public int delayCounter;
	public AbilityCard delayedAbility;
	public int loyalty;

	public Ally (Game game, String name, int cost,
	             CounterMap<Types.Knowledge> knowledge,
	             String text, int health, String owner) {

		super(game, name, knowledge, CardType.Ally, text, owner);

		playable = new Playable(game, this, cost).setSlow().setPersistent();
		studiable = new Studiable(game, this);
		combat = new Combat(game, this, health);
		triggering = new Triggering(game, this);

		setDefaultUnitCheckEvent();

		delayCounter = 0;
		loyalty = 0;
		delayedAbility = null;
	}

	private void setDefaultUnitCheckEvent () {
		triggering.addEventChecker(new EventChecker(game, this,
				event -> {
				if (delayCounter > 0) {
				decreaseDelayCounter(game, 1);
			}
		}).addStartOfControllerTurnChecker());
	}

	public void decreaseDelayCounter (Game game, int count) {
		delayCounter = max(0, delayCounter - count);
		if (delayCounter == 0) {
			newTurn();
			if (delayedAbility != null) {
				game.addToStack(delayedAbility);
				delayedAbility = null;
			}
		}
	}

	@Override
	public void newTurn () {
		if (delayCounter == 0) {
			super.newTurn();
		}
	}

	@Override
	public Types.Card.Builder toCardMessage () {
		return super.toCardMessage()
				.setDelay(delayCounter)
				.setLoyalty(loyalty);
	}

/*
	@Override
	public void copyPropertiesFrom (Card c) {
		super.copyPropertiesFrom(c);
		if (c instanceof Ally) {
			delayCounter = ((Ally) c).delayCounter;
			loyalty = ((Ally) c).loyalty;

		}
	}
*/
}
