package com.visitor.card.properties;

import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.visitor.game.Game.Zone.Void;
import static com.visitor.game.Game.Zone.*;
import static com.visitor.helpers.Predicates.and;

public class Playable {

	public Card card;
	private Game game;

	private Arraylist<UUID> targets;

	private int cost;

	private Arraylist<Supplier<Boolean>> canPlayAdditional;
	private Supplier<Boolean> canPlayTiming;
	private Supplier<Boolean> canPlayResource;

	private Arraylist<Runnable> beforePlay;
	private Arraylist<Consumer<Boolean>> play;
	private Arraylist<Runnable> afterPlay;

	private Runnable resolveEffect;
	private Runnable resolvePlaceCard;
	private Arraylist<Runnable> afterResolve;


	public Playable (Game game, Card card, int cost) {
		this.card = card;
		this.game = game;
		this.cost = cost;
		this.targets = new Arraylist<>();
		this.afterResolve = new Arraylist<>();
		this.canPlayAdditional = new Arraylist<>();
		this.beforePlay = new Arraylist<>();
		this.play = new Arraylist<>();
		this.afterPlay = new Arraylist<>();

		// Default Implementations
		setDefaultCanPlayResource();
		setDefaultCanPlayTiming();
		setDefaultResolveEffect();
		setDefaultResolvePlaceCard();
		setDefaultPlay();
	}

	public Playable (Game game, Card card) {
		this(game, card, 0);
		setNotPlayable();
	}

	public Playable (Game game, Card card, int cost, Runnable resolveEffect) {
		this(game, card, cost);
		this.resolveEffect = resolveEffect;
	}


	public Playable setNotPlayable () {
		canPlayAdditional.add(() -> false);
		return this;
	}

	public Playable setSlow () {
		canPlayTiming = () -> game.canPlaySlow(card.controller);
		return this;
	}

	public Playable setFast () {
		canPlayTiming = () -> true;
		return this;
	}

	private Playable setResolveZone (Game.Zone zone){
		resolvePlaceCard = () -> {
			card.zone = zone;
			game.putTo(card.controller, card, zone);
			if (zone == Play){
				card.enterPlay();
			}
		};
		return this;
	}

	public Playable setPersistent () {
		return setResolveZone(Play);
	}

	public Playable setEphemeral () {
		return setResolveZone(Discard_Pile);
	}

	public Playable setPurging () {
		return setResolveZone(Void);
	}

	public Playable setDisappearing () {
		resolvePlaceCard = () -> {card.zone = null;};
		return this;
	}

	/**
	 * Called by client to check if you can play this card in current game state.
	 *
	 * @return
	 */
	public final boolean canPlay (boolean withResources) {
		boolean result = !withResources || (canPlayResource.get() && canPlayTiming.get());
		int index = -1;
		while (result && index < canPlayAdditional.size() - 1) {
			index++;
			result = result && canPlayAdditional.get(index).get();
		}
		return result;
	}


	/**
	 * Called by server when this card is played.
	 * Default behavior is that it deducts the energy cost of the card,
	 * removes it from player's hand and then puts on the stack.
	 */
	public final void play (boolean withCost) {
		beforePlay.forEachInOrder(Runnable::run);
		play.forEachInOrder(p -> p.accept(withCost));
		afterPlay.forEachInOrder(Runnable::run);
	}

	/**
	 * This is the function that describes what is the effect of the card when it is resolved.
	 * This function contains the business logic of the card effect.
	 */
	public final void resolve () {
		resolveEffect.run();
		resolvePlaceCard.run();
		afterResolve.forEachInOrder(Runnable::run);
	}

	private void setDefaultPlay () {
		play.add((withCost) -> {
			if (withCost) {
				game.spendEnergy(card.controller, cost);
			}
			game.addToStack(card);
		});
	}

	private void setDefaultResolvePlaceCard () {
		setEphemeral();
	}

	private void setDefaultResolveEffect () {
		resolveEffect = () -> {
		};
	}

	private void setDefaultCanPlayTiming () {
		setFast();
	}

	public void setDefaultCanPlayResource () {
		canPlayResource =
				() -> game.hasEnergy(card.controller, cost) &&
						game.hasKnowledge(card.controller, card.knowledge);
	}

	public Playable addCanPlayAdditional (Supplier<Boolean> ...canPlayAdditional) {
		this.canPlayAdditional.addAll(Arrays.asList(canPlayAdditional));
		return this;
	}

	public void setCanPlayTiming (Supplier<Boolean> canPlayTiming) {
		this.canPlayTiming = canPlayTiming;
	}

	public void setCanPlayResource (Supplier<Boolean> canPlayResource) {
		this.canPlayResource = canPlayResource;
	}

	public Playable addBeforePlay (Runnable ...beforePlay) {
		this.beforePlay.addAll(Arrays.asList(beforePlay));
		return this;
	}

	public void addPlay (Consumer<Boolean> ...play) {
		this.play.addAll(Arrays.asList(play));
	}

	public void addAfterPlay (Runnable ...afterPlay) {
		this.afterPlay.addAll(Arrays.asList(afterPlay));
	}

	public Playable setResolveEffect (Runnable resolveEffect) {
		this.resolveEffect = resolveEffect;
		return this;
	}

	public void setResolvePlaceCard (Runnable resolvePlaceCard) {
		this.resolvePlaceCard = resolvePlaceCard;
	}

	public Playable addAfterResolve (Runnable ...afterResolve) {
		this.afterResolve.addAll(Arrays.asList(afterResolve));
		return this;
	}

	public int getCost () {
		return cost;
	}

	// Setters
	public void setCost (int cost) {
		this.cost = cost;
	}

	/** Multiple Targets Setters
	 *
	 */
	// For targeting MULTIPLE CARDS from a zone and/or PLAYERS.
	public void setTargetMultipleCardsOrPlayers (Game.Zone zone, Predicate<Card> cardPredicate, Predicate<Player> playerPredicate, Integer count,
	                                             Boolean upTo, String message, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect, boolean withPlayers) {

		if (zone == null){
			zone = Both_Play;
		}
		if (cardPredicate == null){
			cardPredicate = Predicates::any;
		}
		if (playerPredicate == null){
			playerPredicate = Predicates::any;
		}
		if (afterTargetsEffect == null){
			afterTargetsEffect = ()->{};
		}
		if (count == null){
			count = 1;
		}
		if (upTo == null){
			upTo = false;
		}
		if (message == null){
			message = "Select " + (upTo?"up to ":"") +
					(count == 1? "a card ": count + " cards ") + (withPlayers?" or a player.":".");
		}

		Game.Zone finalZone = zone;
		Predicate<Card> finalCardPredicate = cardPredicate;
		Predicate<Player> finalPlayerPredicate = playerPredicate;
		Boolean finalUpTo = upTo;
		Integer finalCount = count;
		String finalMessage = message;
		Runnable finalAfterTargetsEffect = afterTargetsEffect;

		if (!withPlayers) {
			addCanPlayAdditional(() ->
					game.hasIn(card.controller, finalZone, finalCardPredicate, finalUpTo ? 1 : finalCount)
			);
		}

		addBeforePlay(() -> {
			if (withPlayers) {
				targets.addAll(game.selectFromZoneWithPlayers(card.controller, finalZone, finalCardPredicate, finalPlayerPredicate, finalCount, finalUpTo, finalMessage));
			} else {
				targets.addAll(game.selectFromZone(card.controller, finalZone, finalCardPredicate, finalCount, finalUpTo, finalMessage));
			}
			}
		);
		setResolveEffect(() -> {
			targets.forEach(targetId -> {
				if ((withPlayers && game.isPlayer(targetId)) || game.isIn(card.controller, finalZone, targetId)) {
					perTargetEffect.accept(targetId);
				}
			});
			finalAfterTargetsEffect.run();
		});
	}

	// For targeting MULTIPLE CARDS from a zone.
	public void setTargetMultipleCards (Game.Zone zone, Predicate<Card> cardPredicate,
	                                    int count, boolean upTo, String message,
	                                    Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetMultipleCardsOrPlayers(zone, cardPredicate, null, count, upTo, message, perTargetEffect, afterTargetsEffect, false);
	}

	// For targeting MULTIPLE UNITS from a zone.
	public void setTargetMultipleUnits (Game.Zone zone, int count, boolean upTo, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetMultipleCards(zone, Predicates::isUnit, count, upTo, "Select " + (upTo ? " up to " : "") + (count > 1 ? count + " units." : "a unit."), perTargetEffect, afterTargetsEffect);
	}

	/** Single Target Setters
	 *
	 */
	// For targeting a SINGLE CARD with RESTRICTIONS from a zone or a PLAYER.
	public void setTargetSingleCardOrPlayer (Game.Zone zone, Predicate<Card> cardPredicate, Predicate<Player> playerPredicate,
	                                         String message, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect, boolean withPlayers) {
		if (message == null){
			message = "Select a card" + (withPlayers?" or a player.":".");
		}
		setTargetMultipleCardsOrPlayers(zone, cardPredicate, playerPredicate, null, null, message, perTargetEffect, afterTargetsEffect, withPlayers);
	}

	// For targeting a SINGLE CARD with RESTRICTIONS from a zone.
	public void setTargetSingleCard (Game.Zone zone, Predicate<Card> cardPredicate,
	                                 String message, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {

		setTargetSingleCardOrPlayer(zone, cardPredicate, null, message, perTargetEffect, afterTargetsEffect, false);
	}

	// For targeting a SINGLE UNIT from a zone or a PLAYER.
	public void setTargetSingleUnitOrPlayer (Game.Zone zone, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleCardOrPlayer(zone, Predicates::isUnit, null, null, perTargetEffect, afterTargetsEffect, true);
	}

	/**
	 * For targeting a SINGLE UNIT with RESTRICTIONS from a zone.
	 * @param zone = Both_Play
	 * @param cardPredicate = any
	 * @param perTargetEffect
	 * @param afterTargetsEffect = ()->{}
	 */
	public void setTargetSingleUnit (Game.Zone zone, Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleCard(zone, and(Predicates::isUnit, cardPredicate), "Select a unit.", perTargetEffect, afterTargetsEffect);
	}

	// For targeting a SINGLE PLAYER.
	public void setTargetSinglePlayer (Consumer<UUID> playerEffect) {
		addBeforePlay(() ->
				targets.addAll(game.selectPlayers(card.controller, Predicates::any, 1, false))
		);
		setResolveEffect(() -> playerEffect.accept(targets.get(0)));
	}



}
