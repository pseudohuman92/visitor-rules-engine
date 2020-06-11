package com.visitor.card.properties;

import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Predicates;

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

	private Supplier<Boolean> canPlayAdditional;
	private Supplier<Boolean> canPlayTiming;
	private Supplier<Boolean> canPlayResource;

	private Runnable beforePlay;
	private Runnable play;
	private Runnable afterPlay;

	private Runnable resolveEffect;
	private Runnable resolvePlaceCard;
	private Runnable afterResolve;


	public Playable (Game game, Card card, int cost) {
		this.card = card;
		this.game = game;
		this.cost = cost;
		this.targets = new Arraylist<>();

		// Default Implementations
		setDefaultCanPlayResource();
		setDefaultCanPlayTiming();
		setDefaultCanPlayAdditional();

		setDefaultResolveEffect();
		setDefaultResolvePlaceCard();
		setDefaultAfterResolve();

		setDefaultBeforePlay();
		setDefaultPlay();
		setDefaultAfterPlay();

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
		canPlayAdditional = () -> false;
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

	public Playable setPersistent () {
		resolvePlaceCard = () -> game.putTo(card.controller, card, Play);
		return this;
	}

	public Playable setEphemeral () {
		resolvePlaceCard = () -> game.putTo(card.controller, card, Discard_Pile);
		return this;
	}

	public Playable setPurging () {
		resolvePlaceCard = () -> game.putTo(card.controller, card, Void);
		return this;
	}

	/**
	 * Called by client to check if you can play this card in current game state.
	 *
	 * @return
	 */
	public final boolean canPlay () {
		return canPlayResource.get() && canPlayTiming.get() && canPlayAdditional.get();
	}


	/**
	 * Called by server when this card is played.
	 * Default behavior is that it deducts the energy cost of the card,
	 * removes it from player's hand and then puts on the stack.
	 */
	public final void play () {
		beforePlay.run();
		play.run();
		afterPlay.run();
	}

	/**
	 * This is the function that describes what is the effect of the card when it is resolved.
	 * This function contains the business logic of the card effect.
	 */
	public final void resolve () {
		resolveEffect.run();
		resolvePlaceCard.run();
		afterResolve.run();
	}

	// Default Setters
	private void setDefaultAfterPlay () {
		afterPlay = () -> {
		};
	}

	private void setDefaultPlay () {
		play = () -> {
			game.spendEnergy(card.controller, cost);
			game.addToStack(card);
		};
	}

	private void setDefaultBeforePlay () {
		beforePlay = () -> {
		};
	}

	private void setDefaultResolvePlaceCard () {
		setEphemeral();
	}

	private void setDefaultResolveEffect () {
		resolveEffect = () -> {
		};
	}

	private void setDefaultAfterResolve () {
		afterResolve = () -> {
		};
	}

	private void setDefaultCanPlayAdditional () {
		canPlayAdditional = () -> true;
	}

	private void setDefaultCanPlayTiming () {
		setFast();
	}

	public void setDefaultCanPlayResource () {
		canPlayResource =
				() -> game.hasEnergy(card.controller, cost) &&
						game.hasKnowledge(card.controller, card.knowledge);
	}

	public Playable setCanPlayAdditional (Supplier<Boolean> canPlayAdditional) {
		this.canPlayAdditional = canPlayAdditional;
		return this;
	}

	public void setCanPlayTiming (Supplier<Boolean> canPlayTiming) {
		this.canPlayTiming = canPlayTiming;
	}

	public void setCanPlayResource (Supplier<Boolean> canPlayResource) {
		this.canPlayResource = canPlayResource;
	}

	public Playable setBeforePlay (Runnable beforePlay) {
		this.beforePlay = beforePlay;
		return this;
	}

	public void setPlay (Runnable play) {
		this.play = play;
	}

	public void setAfterPlay (Runnable afterPlay) {
		this.afterPlay = afterPlay;
	}

	public Playable setResolveEffect (Runnable resolveEffect) {
		this.resolveEffect = resolveEffect;
		return this;
	}

	public void setResolvePlaceCard (Runnable resolvePlaceCard) {
		this.resolvePlaceCard = resolvePlaceCard;
	}

	public Playable setAfterResolve (Runnable afterResolve) {
		this.afterResolve = afterResolve;
		return this;
	}

	public Playable addAfterResolve (Runnable afterResolve) {
		Runnable oldAfterResolve = this.afterResolve;
		this.afterResolve = () -> {
			oldAfterResolve.run();
			afterResolve.run();
		};
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
	public void setTargetMultipleCardsOrPlayers (Game.Zone zone, Predicate<Card> cardPredicate, Predicate<Player> playerPredicate, int count,
	                                             boolean upTo, String message, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect, boolean withPlayers) {
		if (!withPlayers) {
			setCanPlayAdditional(() ->
					game.hasIn(card.controller, zone, cardPredicate, upTo ? 1 : count)
			);
		}
		setBeforePlay(() -> {
			if (withPlayers) {
				targets.addAll(game.selectFromZoneWithPlayers(card.controller, zone, cardPredicate, playerPredicate, count, upTo, message));
			} else {
				targets.addAll(game.selectFromZone(card.controller, zone, cardPredicate, count, upTo, message));
			}
			}
		);
		setResolveEffect(() -> {
			targets.forEach(targetId -> {
				if ((withPlayers && game.isPlayer(targetId)) || game.isIn(card.controller, zone, targetId)) {
					perTargetEffect.accept(targetId);
				}
			});
			afterTargetsEffect.run();
		});
	}

	// For targeting MULTIPLE CARDS from a zone.
	public void setTargetMultipleCards (Game.Zone zone, Predicate<Card> cardPredicate,
	                                    int count, boolean upTo, String message,
	                                    Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetMultipleCardsOrPlayers(zone, cardPredicate, null, count, upTo, message, perTargetEffect, afterTargetsEffect, false);
	}
	public void setTargetMultipleCards (Game.Zone zone, Predicate<Card> cardPredicate,
	                                    int count, boolean upTo, String message,
	                                    Consumer<UUID> perTargetEffect) {
		setTargetMultipleCards(zone, cardPredicate, count, upTo, message, perTargetEffect, ()->{});
	}

	// For targeting MULTIPLE UNITS from a zone.
	public void setTargetMultipleUnits (Game.Zone zone, int count, boolean upTo, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetMultipleCards(zone, Predicates::isUnit, count, upTo, "Select " + (upTo ? " up to " : "") + (count > 1 ? count + " units." : "a unit."), perTargetEffect, afterTargetsEffect);
	}
	public void setTargetMultipleUnits (Game.Zone zone, int count, boolean upTo, Consumer<UUID> effect) {
		setTargetMultipleUnits(zone, count, upTo, effect, () -> {
		});
	}

	// For targeting MULTIPLE UNITS in PLAY.
	public void setTargetMultipleUnits (int count, boolean upTo, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetMultipleUnits(Both_Play, count, upTo, perTargetEffect, afterTargetsEffect);
	}
	public void setTargetMultipleUnits (int count, boolean upTo, Consumer<UUID> effect) {
		setTargetMultipleUnits(count, upTo, effect, () -> {
		});
	}

	/** Single Target Setters
	 *
	 */
	// For targeting a SINGLE CARD with RESTRICTIONS from a zone or a PLAYER.
	public void setTargetSingleCardOrPlayer (Game.Zone zone, Predicate<Card> cardPredicate, Predicate<Player> playerPredicate,
	                                         String message, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect, boolean withPlayers) {
		setTargetMultipleCardsOrPlayers(zone, cardPredicate, playerPredicate, 1, false, message, perTargetEffect, afterTargetsEffect, withPlayers);
	}

	// For targeting a SINGLE CARD with RESTRICTIONS from a zone.
	public void setTargetSingleCard (Game.Zone zone, Predicate<Card> cardPredicate,
	                                 String message, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleCardOrPlayer(zone, cardPredicate, null, message, perTargetEffect, afterTargetsEffect, false);
	}
	public void setTargetSingleCard (Game.Zone zone, Predicate<Card> cardPredicate,
	                                 String message, Consumer<UUID> perTargetEffect) {
		setTargetSingleCard(zone, cardPredicate, message, perTargetEffect, ()->{});
	}

	// For targeting a SINGLE CARD from a zone. No aftereffects.
	public void setTargetSingleCard (Game.Zone zone, String message, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleCard(zone, Predicates::any, message, perTargetEffect, afterTargetsEffect);
	}
	public void setTargetSingleCard (Game.Zone zone, String message, Consumer<UUID> perTargetEffect) {
		setTargetSingleCard(zone, message, perTargetEffect, ()->{});
	}

	// For targeting a SINGLE UNIT from a zone or a PLAYER.
	public void setTargetSingleUnitOrPlayer (Game.Zone zone, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleCardOrPlayer(zone, Predicates::isUnit, Predicates::any, "Select a unit or a player.", perTargetEffect, afterTargetsEffect, true);
	}
	public void setTargetSingleUnitOrPlayer (Game.Zone zone, Consumer<UUID> perTargetEffect) {
		setTargetSingleUnitOrPlayer(zone, perTargetEffect, () -> {
		});
	}

	// For targeting a SINGLE UNIT IN PLAY or a PLAYER.
	public void setTargetSingleUnitOrPlayer (Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleUnitOrPlayer(Both_Play, perTargetEffect, afterTargetsEffect);
	}
	public void setTargetSingleUnitOrPlayer (Consumer<UUID> perTargetEffect) {
		setTargetSingleUnitOrPlayer(perTargetEffect, () -> {
		});
	}

	// For targeting a SINGLE UNIT with RESTRICTIONS from a zone.
	public void setTargetSingleUnit (Game.Zone zone, Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleCard(zone, and(Predicates::isUnit, cardPredicate), "Select a unit.", perTargetEffect, afterTargetsEffect);
	}
	public void setTargetSingleUnit (Game.Zone zone, Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect) {
		setTargetSingleUnit(zone, cardPredicate, perTargetEffect, () -> {
		});
	}

	// For targeting a SINGLE UNIT from a zone.
	public void setTargetSingleUnit (Game.Zone zone, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleUnit(zone, Predicates::any, perTargetEffect, afterTargetsEffect);
	}
	public void setTargetSingleUnit (Game.Zone zone, Consumer<UUID> perTargetEffect) {
		setTargetSingleUnit(zone, perTargetEffect, ()->{});
	}

	// For targeting a SINGLE UNIT with RESTRICTIONS in PLAY.
	public void setTargetSingleUnit (Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleUnit(Both_Play, cardPredicate, perTargetEffect, afterTargetsEffect);
	}
	public void setTargetSingleUnit (Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect) {
		setTargetSingleUnit(cardPredicate, perTargetEffect, () -> {
		});
	}

	// For targeting a SINGLE UNIT in PLAY.
	public void setTargetSingleUnit (Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleUnit(Predicates::any, perTargetEffect, afterTargetsEffect);
	}
	public void setTargetSingleUnit (Consumer<UUID> perTargetEffect) {
		setTargetSingleUnit(Predicates::any, perTargetEffect);
	}

	// For targeting a SINGLE CARD with RESTRICTIONS in STACK.
	public void setTargetSingleCardInStack (Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetSingleCard(Stack, cardPredicate, "Select a card from stack.", perTargetEffect, afterTargetsEffect);
	}
	public void setTargetSingleCardInStack (Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect) {
		setTargetSingleCardInStack(cardPredicate, perTargetEffect, () -> {
		});
	}

	// For targeting a SINGLE PLAYER.
	public void setTargetSinglePlayer (Consumer<UUID> playerEffect) {
		setBeforePlay(() ->
				targets.addAll(game.selectPlayers(card.controller, Predicates::any, 1, false))
		);
		setResolveEffect(() -> playerEffect.accept(targets.get(0)));
	}
}
