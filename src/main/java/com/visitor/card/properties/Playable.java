package com.visitor.card.properties;

import com.visitor.card.containers.Effect;
import com.visitor.card.Card;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.UUIDHelper;
import com.visitor.protocol.Types;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.visitor.game.parts.Base.Zone.*;
import static com.visitor.helpers.Predicates.*;

public class Playable {

    public Card card;
    private final Game game;
    private int cost; //Energy cost

    private final Arraylist<Supplier<Boolean>> playAdditionalConditions;
    private Supplier<Boolean> playTimingCondition;
    private Supplier<Boolean> playResourceCondition;

    private final Hashmap<UUID, Effect> costEffects; //For paying additional costs
    private final Arraylist<Consumer<Boolean>> play;  //Pays the energy cost and places card into the stack.

    private Runnable resolvePlaceCard; //Places card into its resolve zone
    private final Hashmap<UUID, Effect> afterResolveEffects;   //Runs after card is placed into its resolve zone

    private final Hashmap<UUID, Effect> resolveEffects;
    public Playable(Game game, Card card, int cost) {
        this.card = card;
        this.game = game;
        this.cost = cost;
        this.afterResolveEffects = new Hashmap<>();
        this.playAdditionalConditions = new Arraylist<>();
        this.play = new Arraylist<>();
        this.costEffects = new Hashmap<>();
        this.resolveEffects = new Hashmap<>();
        
        // Default Implementations
        setDefaultPlayResourceCondition();
        setDefaultPlayTimingResource();
        setDefaultResolvePlaceCard();
        setDefaultPlay();
        setDefaultPlayAdditionalConditions();
    }

    public Playable(Game game, Card card) {
        this(game, card, 0);
        setNotPlayable();
    }

    public Playable(Game game, Card card, int cost, Runnable resolveEffect) {
        this(game, card, cost);
        addResolveEffect(resolveEffect);
    }


    public Playable setNotPlayable() {
        playAdditionalConditions.add(() -> false);
        return this;
    }

    public Playable setSlow() {
        playTimingCondition = () -> game.canPlaySlow(card.controller);
        return this;
    }

    public Playable setFast() {
        playTimingCondition = () -> game.canPlayFast(card.controller);
        return this;
    }

    private Playable setResolveZone(Game.Zone zone) {
        resolvePlaceCard = () -> {
            card.zone = zone;
            game.putTo(card.controller, card, zone);
            if (zone == Play) {
                card.enterPlay();
            }
        };
        return this;
    }

    public Playable setPersistent() {
        return setResolveZone(Play);
    }

    public Playable setEphemeral() {
        return setResolveZone(Discard_Pile);
    }

    public Playable setDisappearing() {
        resolvePlaceCard = () -> {
            card.zone = None;
        };
        return this;
    }

    /**
     * Called by client to check if you can play this card in current game state.
     */
    public final boolean canPlay(boolean withResources) {
        boolean result = (!withResources || (playResourceCondition.get() && playTimingCondition.get())) &&
                card.zone == Hand;
        for (Supplier<Boolean> additionalCondition : playAdditionalConditions) {
            result = result && additionalCondition.get();
        }
        return result;
    }


    /**
     * Called by server when this card is played.
     * Default behavior is that it deducts the energy cost of the card,
     * removes it from player's hand and then puts on the stack.
     */
    public final void play(boolean withCost) {
        play.forEach(p -> p.accept(withCost));
    }

    /**
     * This is the function that describes what is the effect of the card when it is resolved.
     * This function contains the business logic of the card effect.
     */
    public final void resolve() {
        resolveEffects.values().forEach(Effect::runEffect);
        resolvePlaceCard.run();
        afterResolveEffects.values().forEach(Effect::runEffect);
    }


    private boolean compareZones(Base.Zone z1, Base.Zone z2){
        if (z1 == Both_Play || z1 == Both_Play_With_Players || z1 == Play_With_Player)
            z1 = Play;
        if (z2 == Both_Play || z2 == Both_Play_With_Players || z2 == Play_With_Player)
            z2 = Play;
        return z1 == z2;
    }
    /**
     * Multiple Targets Setters
     */
    public void addTargetMultipleCardsOrPlayers(Game.Zone zone, Predicate<Targetable> predicate,
                                                int minCount, int maxCount, String message, Consumer<UUID> perTargetEffect, boolean withPlayers, boolean forCost) {

        String targetingMessage = message != null ? message : "Select " + (minCount < maxCount ? "between " + minCount + " and " + maxCount : minCount) + " cards" + (withPlayers ? " or players." : ".");

        Effect t = new Effect(game, card, zone, minCount, maxCount, predicate, targetingMessage, perTargetEffect);
        if (forCost) {
            costEffects.put(t.getId(), t);
        } else {
            resolveEffects.put(t.getId(), t);
        }
    }

    // For targeting MULTIPLE CARDS from a zone.
    public void addTargetMultipleCards(Game.Zone zone, Predicate<Targetable> cardPredicate,
                                       int minCount, int maxCount, String message,
                                       Consumer<UUID> perTargetEffect, boolean forCost) {
        addTargetMultipleCardsOrPlayers(zone, and(Predicates::isCard, cardPredicate), minCount, maxCount, message, perTargetEffect, false, forCost);
    }

    // For targeting MULTIPLE UNITS from a zone.
    public void addTargetMultipleUnits(Game.Zone zone, int minCount, int maxCount, Consumer<UUID> perTargetEffect, boolean forCost) {
        addTargetMultipleCards(zone, Predicates::isUnit, minCount, maxCount, "Select " +  (minCount < maxCount ? "between " + minCount + " and " + maxCount : minCount) + " units.", perTargetEffect, forCost);
    }

    /**
     * Single Target Setters
     */
    // For targeting a SINGLE CARD with RESTRICTIONS from a zone or a PLAYER.
    public void addTargetSingleCardOrPlayer(Game.Zone zone, Predicate<Targetable> predicate,
                                            String message, Consumer<UUID> perTargetEffect, boolean withPlayers, boolean forCost) {
        String finalMessage = message != null ? message : "Select a card" + (withPlayers ? " or a player." : ".");
        addTargetMultipleCardsOrPlayers(zone, predicate, 1, 1, finalMessage, perTargetEffect, withPlayers, forCost);
    }

    // For targeting a SINGLE CARD with RESTRICTIONS from a zone.
    public void addTargetSingleCard(Game.Zone zone, Predicate<Targetable> predicate,
                                    String message, Consumer<UUID> perTargetEffect, boolean forCost) {

        addTargetSingleCardOrPlayer(zone, Predicates.and(Predicates::isCard, predicate), message, perTargetEffect, false, forCost);
    }

    // For targeting a SINGLE UNIT from a zone or a PLAYER.
    public void addTargetSingleUnitOrPlayer(Game.Zone zone, Predicate<Targetable> predicate, Consumer<UUID> perTargetEffect, boolean forCost) {
        addTargetSingleCardOrPlayer(zone, and (predicate, or(Predicates::isPlayer, Predicates::isUnit)), null, perTargetEffect, true, forCost);
    }

    public void addTargetSingleUnitOrPlayer(Game.Zone zone, Consumer<UUID> perTargetEffect, boolean forCost) {
        addTargetSingleCardOrPlayer(zone, or(Predicates::isPlayer, Predicates::isUnit), null, perTargetEffect, true, forCost);
    }

    public void addTargetSingleUnitOrPlayer(Consumer<UUID> perTargetEffect, boolean forCost) {
        addTargetSingleCardOrPlayer(Both_Play_With_Players, or(Predicates::isPlayer, Predicates::isUnit), null, perTargetEffect, true, forCost);
    }

    /**
     * For targeting a SINGLE UNIT with RESTRICTIONS from a zone.
     *
     * @param zone               = Both_Play
     * @param perTargetEffect
     */
    public void addTargetSingleUnit(Game.Zone zone, Predicate<Targetable> predicate, Consumer<UUID> perTargetEffect, String message, boolean forCost) {
        message = message != null ? message : "Select a unit.";
        addTargetSingleCard(zone, and(Predicates::isUnit, predicate), message, perTargetEffect, forCost);
    }


    /**
     * Default values for class attributes.
     */
    private void setDefaultPlay() {
        play.add((withCost) -> {
            if (withCost) {
                game.removeEnergy(card.controller, cost);
            }
            for (Effect e : costEffects.values()){
                e.runEffect();
            }
            game.addToStack(card);
        });
    }

    private void setDefaultResolvePlaceCard() {
        setEphemeral();
    }

    private void setDefaultPlayTimingResource() {
        setFast();
    }

    public void setDefaultPlayResourceCondition() {
        playResourceCondition =
                () -> game.hasEnergy(card.controller, cost) &&
                        game.hasKnowledge(card.controller, card.knowledge);
    }

    public void setDefaultPlayAdditionalConditions() {
        playAdditionalConditions.add(() -> {
            boolean canPlay = true;
            for (Effect value : costEffects.values()) {
                canPlay = canPlay && value.hasEnoughTargets();
            }
            for (Effect value : resolveEffects.values()) {
                canPlay = canPlay && value.hasEnoughTargets();
            }
            return canPlay;
        });
    }

    /**
     * Adders and setters of attributes
     */
    public final Playable addPlayAdditionalConditions(Supplier<Boolean>... canPlayAdditional) {
        this.playAdditionalConditions.addAll(Arrays.asList(canPlayAdditional));
        return this;
    }

    public void setPlayTimingCondition(Supplier<Boolean> playTimingCondition) {
        this.playTimingCondition = playTimingCondition;
    }

    public void setPlayResourceCondition(Supplier<Boolean> playResourceCondition) {
        this.playResourceCondition = playResourceCondition;
    }

    public final void addPlay(Consumer<Boolean>... play) {
        this.play.addAll(Arrays.asList(play));
    }

    public Playable addResolveEffect(Runnable resolveEffect) {
        Effect t = new Effect(game, card, resolveEffect);
        this.resolveEffects.put(t.getId(), t);
        return this;
    }

    public void setResolvePlaceCard(Runnable resolvePlaceCard) {
        this.resolvePlaceCard = resolvePlaceCard;
    }

    public Playable addAfterResolveEffect(Runnable afterResolveEffect) {
        Effect t = new Effect(game, card, afterResolveEffect);
        this.afterResolveEffects.put(t.getId(), t);
        return this;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public int getCost() {
        return cost;
    }

    public boolean needsTargets() {return !resolveEffects.isEmpty(); }

    public Arraylist<Types.Targeting> getTargetingBuilder() {
        Arraylist<Types.Targeting> builders = new Arraylist<>();
        for (Effect t : costEffects.values())
            builders.add(t.toTargetingBuilder().build());
        for (Effect t : resolveEffects.values())
            builders.add(t.toTargetingBuilder().build());
        return builders;
    }

    public void clear() {
        for (Effect t : costEffects.values()){
            t.clear();
        }
        for (Effect t : resolveEffects.values()){
            t.clear();
        }
    }

    public void setTargets(Arraylist<Types.TargetSelection> targets) {
        for (Types.TargetSelection t : targets) {
            Effect tr = costEffects.get(UUID.fromString(t.getId()));
            if (tr != null){
                tr.setTargets(UUIDHelper.toUUIDList(t.getTargetsList()));
            } else {
                tr = resolveEffects.get(UUID.fromString(t.getId()));
                if (tr != null){
                    tr.setTargets(UUIDHelper.toUUIDList(t.getTargetsList()));
                } else {
                    tr = afterResolveEffects.get(UUID.fromString(t.getId()));
                    if (tr != null){
                        tr.setTargets(UUIDHelper.toUUIDList(t.getTargetsList()));
                    }
                }
            }
        }
    }

    public Arraylist<UUID> getAllTargets() {
        Arraylist<UUID> targets = new Arraylist<>();
        for (Effect t : costEffects.values()){
            targets.addAll(t.getTargets());
        }
        for (Effect t : resolveEffects.values()){
            targets.addAll(t.getTargets());
        }
        for (Effect t : afterResolveEffects.values()){
            targets.addAll(t.getTargets());
        }
        return targets;
    }
}
