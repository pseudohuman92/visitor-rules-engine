package com.visitor.card.properties;

import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Predicates;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.visitor.game.Game.Zone.Void;
import static com.visitor.game.Game.Zone.*;

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
	public void resolve () {
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

	public void setTargetingResolveFromZone (Game.Zone zone, Predicate<Card> cardPredicate, int count, boolean upTo, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		if (!upTo) {
			setCanPlayAdditional(() ->
					game.hasIn(card.controller, zone, cardPredicate, count)
			);
		}
		setBeforePlay(() ->
				targets.addAll(game.selectFromZone(card.controller, zone, cardPredicate, count, upTo))
		);
		setResolveEffect(() -> {
			targets.forEach(targetId -> {
				if (game.isIn(card.controller, zone, targetId)) {
					perTargetEffect.accept(targetId);
				}
			});
			afterTargetsEffect.run();
		});
	}

	public void setTargetingMultipleUnitsInBothPlay (int count, boolean upTo, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetingResolveFromZone(Both_Play, Predicates::isUnit, count, upTo, perTargetEffect, afterTargetsEffect);
	}

	public void setTargetingMultipleUnitsInBothPlay (int count, boolean upTo, Consumer<UUID> effect) {
		setTargetingMultipleUnitsInBothPlay(count, upTo, effect, ()-> {});
	}

	public void setTargetingSingleUnitInZone (Game.Zone zone, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetingResolveFromZone(zone, Predicates::isUnit, 1, false, perTargetEffect, afterTargetsEffect);
	}

	public void setTargetingSingleUnitInZone (Game.Zone zone, Consumer<UUID> perTargetEffect) {
		setTargetingSingleUnitInZone(zone, perTargetEffect, ()->{});
	}

	public void setTargetingSingleUnit (Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetingSingleUnitInZone(Both_Play, perTargetEffect, afterTargetsEffect);
	}

	public void setTargetingSingleUnit (Consumer<UUID> perTargetEffect) {
		setTargetingSingleUnit(perTargetEffect, ()->{});
	}

	public void setTargetingSingleCardInStack (Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect, Runnable afterTargetsEffect) {
		setTargetingResolveFromZone(Stack, cardPredicate, 1, false, perTargetEffect, afterTargetsEffect);
	}

	public void setTargetingSingleCardInStack (Predicate<Card> cardPredicate, Consumer<UUID> perTargetEffect) {
		setTargetingSingleCardInStack(cardPredicate, perTargetEffect, ()->{});
	}

	public void setSinglePlayerTargetingResolve(Consumer<UUID> playerEffect) {
		setBeforePlay(() ->
				targets.addAll(game.selectPlayers(card.controller, Predicates::any, 1, false))
		);
				setResolveEffect(() -> playerEffect.accept(targets.get(0)));
	}

	// Setters
	public void setCost (int cost) {
		this.cost = cost;
	}
}
