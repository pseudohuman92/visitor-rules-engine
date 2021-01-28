/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.properties;

import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Interface for cards that has an activating ability.
 *
 * @author pseudo
 */
public class Attachable {

	private final Card card;
	private final Game game;

	private Predicate<Card> validTarget;
	private UUID attachedTo;
	private Consumer<UUID> afterAttached;
	private Consumer<UUID> afterRemoved;

	// Constructors
	public Attachable (Game game, Card card, Predicate<Card> validTarget,
	                   Consumer<UUID> afterAttached, Consumer<UUID> afterRemoved) {
		this.game = game;
		this.card = card;
		this.validTarget = validTarget;
		this.afterAttached = afterAttached;
		this.afterRemoved = afterRemoved;
	}

	public final boolean canAttach (Card candidate) {
		return validTarget.test(candidate);
	}

	public final void setAttachTo (UUID attachedId){
		attachedTo = attachedId;
	}

	public final void attach (){
		game.addAttachmentTo(attachedTo, card.id);
		afterAttached.accept(attachedTo);
	}

	public final void removeFromAttached (){
		game.removeAttachmentFrom(attachedTo, card.id);
		afterRemoved.accept(attachedTo);
		attachedTo = null;
	}

	/*
	public final void activate () {
		Arraylist<ActivatedAbility> abilities = getActivatableAbilities();
		if (abilities.size() == 1) {
			abilities.get(0).activate();
		} else if (abilities.size() > 1) {
			Arraylist<Card> abilityCards = new Arraylist<>(abilities.transform(aa -> (Card)new AbilityCard(game, card, aa)));
			UUID chosenAbility = game.selectFromList(card.controller,  abilityCards, Predicates::any, 1, false, "Choose an ability to activate.").get(0);
			abilities.forEach(aa -> {
				if(aa.id.equals(chosenAbility)){
					aa.activate();
				}
			});
		}
	}
	*/
}
