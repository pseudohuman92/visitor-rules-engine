package com.visitor.helpers.containers;

import com.visitor.card.types.helpers.Ability;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

import java.util.UUID;
import java.util.function.Supplier;

public class ActivatedAbility {

	private Game game;
	public final UUID id;
	private Card card;
	private int cost;
	private boolean depleting;
	private boolean slow;
	private boolean sacrificing;
	private String text;
	private CounterMap<Types.Knowledge> knowledgeRequirement;
	private Supplier<Boolean> canActivateAdditional;
	private Runnable beforeActivate;
	private Runnable activate;

	public ActivatedAbility (Game game, Card card, int cost, String text,
	                         Supplier<Boolean> canActivateAdditional,
	                         Runnable beforeActivate, Runnable activate) {
		this.id = UUID.randomUUID();
		this.game = game;
		this.card = card;
		this.cost = cost;
		this.text = text;
		this.depleting = false;
		this.slow = false;
		this.sacrificing = false;
		this.knowledgeRequirement = new CounterMap<>();
		this.canActivateAdditional = canActivateAdditional;
		this.beforeActivate = beforeActivate;
		this.activate = activate;
	}

	public ActivatedAbility (Game game, Card card, int cost, String text, Runnable activate) {
		this (game, card, cost, text, ()->true, ()->{}, activate);
	}

	public ActivatedAbility (Game game, Card card, int cost, String text, Supplier<Boolean> canActivateAdditional, Runnable activate) {
		this (game, card, cost, text, canActivateAdditional, ()->{}, activate);
	}

	public ActivatedAbility (Game game, Card card, int cost, String text, Runnable beforeActivate, Runnable activate) {
		this (game, card, cost, text, ()->true, beforeActivate, activate);
	}

	public final boolean canActivate(){
		return game.hasKnowledge(card.controller, knowledgeRequirement) &&
					 game.hasEnergy(card.controller, cost) &&
					 (!depleting || (!card.isDepleted() && !card.isDeploying())) &&
					 (!slow || game.canPlaySlow(card.controller)) &&
					 canActivateAdditional.get();
	}

	public final void activate(){
		beforeActivate.run();
		game.spendEnergy(card.controller, cost);
		if (depleting) {
			game.deplete(card.id);
		}
		if (sacrificing) {
			game.sacrifice(card.id);
		}
		game.addToStack(new Ability(game, card, this));
	}

	public ActivatedAbility setSlow(){
		this.slow = true;
		return this;
	}

	public ActivatedAbility setDepleting(){
		this.depleting = true;
		return this;
	}

	public ActivatedAbility setSacrificing(){
		this.sacrificing = true;
		return this;
	}

	public Runnable getActivate () {
		return activate;
	}

	public String getText () {
		return text;
	}

	public ActivatedAbility setKnowledgeRequirement (CounterMap<Types.Knowledge> knowledgeRequirement) {
		this.knowledgeRequirement = knowledgeRequirement;
		return this;
	}

	public void setCost (int x) {
		cost = x;
	}

	public void setBeforeActivate (Runnable beforeActivate) {
		this.beforeActivate = beforeActivate;
	}
}
