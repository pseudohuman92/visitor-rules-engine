package com.visitor.helpers.containers;

import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ActivatedAbility {

    private Game game;
    public final UUID id;
    private Card card;
    private int cost;
    private boolean depleting;
    private boolean slow;
    private boolean selfSacrificing;
    private boolean purging;
    private String text;
    private CounterMap<Types.Knowledge> knowledgeRequirement;
    private Arraylist<Supplier<Boolean>> canActivateAdditional;
    private Arraylist<Runnable> beforeActivate;
    private Runnable activate;

    private Arraylist<UUID> targets;


    public ActivatedAbility(Game game, Card card, int cost, String text) {
        this.id = UUID.randomUUID();
        this.game = game;
        this.card = card;
        this.cost = cost;
        this.text = text;
        this.depleting = false;
        this.slow = false;
        this.selfSacrificing = false;
        this.purging = false;
        this.knowledgeRequirement = new CounterMap<>();
        this.canActivateAdditional = new Arraylist<>();
        this.beforeActivate = new Arraylist<>();
        this.activate = () -> {
        };
        this.targets = new Arraylist<>();
    }

    public ActivatedAbility(Game game, Card card, int cost, String text, Supplier<Boolean> canActivateAdditional, Runnable beforeActivate, Runnable activate) {
        this(game, card, cost, text);
        addCanActivateAdditional(canActivateAdditional);
        addBeforeActivate(beforeActivate);
        setActivate(activate);
    }

    public ActivatedAbility(Game game, Card card, int cost, String text, Runnable beforeActivate, Runnable activate) {
        this(game, card, cost, text);
        addBeforeActivate(beforeActivate);
        setActivate(activate);
    }

    public ActivatedAbility(Game game, Card card, int cost, String text, Supplier<Boolean> canActivateAdditional, Runnable activate) {
        this(game, card, cost, text);
        addCanActivateAdditional(canActivateAdditional);
        setActivate(activate);
    }

    public ActivatedAbility(Game game, Card card, int cost, String text, Runnable activate) {
        this(game, card, cost, text);
        setActivate(activate);
    }

    public final boolean canActivate() {
        boolean canActivate = game.hasKnowledge(card.controller, knowledgeRequirement) &&
                game.hasEnergy(card.controller, cost) &&
                (!depleting || (!card.isDepleted() && !card.isDeploying())) &&
                (!slow || game.canPlaySlow(card.controller));
        for (Supplier<Boolean> caa : canActivateAdditional) {
            canActivate = canActivate && caa.get();
        }
        return canActivate;
    }

    public final void activate() {
        for (Runnable ba : beforeActivate) {
            ba.run();
        }
        game.spendEnergy(card.controller, cost);
        if (depleting) {
            game.deplete(card.id);
        }
        if (selfSacrificing) {
            game.sacrifice(card.id);
        }
        if (purging) {
            game.purge(card.id);
        }
        game.addToStack(new AbilityCard(game, card, this));
    }

    public ActivatedAbility setSlow() {
        this.slow = true;
        return this;
    }

    public ActivatedAbility setDepleting() {
        this.depleting = true;
        return this;
    }

    public ActivatedAbility setSelfSacrificing() {
        this.selfSacrificing = true;
        return this;
    }

    public Runnable getActivate() {
        return activate;
    }

    public String getText() {
        return text;
    }

    public ActivatedAbility setKnowledgeRequirement(CounterMap<Types.Knowledge> knowledgeRequirement) {
        this.knowledgeRequirement = knowledgeRequirement;
        return this;
    }

    public void setCost(int x) {
        cost = x;
    }

    public ActivatedAbility addBeforeActivate(Runnable... beforeActivates) {
        this.beforeActivate.addAll(Arrays.asList(beforeActivates));
        return this;
    }

    // TODO: Refactor cards to use this.
    // Overrides targets
    public ActivatedAbility setTargeting(Game.Zone zone, Predicate<Card> validCards, int count, boolean upTo, Consumer<UUID> targetIdConsumer) {
        if (!upTo) {
            addCanActivateAdditional(() -> game.hasIn(card.controller, zone, validCards, count));
        }
        addBeforeActivate(() -> targets = game.selectFromZone(card.controller, zone, validCards, count, upTo, ""));
        setActivate(() -> targets.forEach(targetIdConsumer::accept));
        return this;
    }

    // Overrides targets
    public ActivatedAbility setTargetingForDamage(int count, boolean upTo, Consumer<UUID> targetIdConsumer) {
        addBeforeActivate(() -> targets = game.selectDamageTargets(card.controller, count, upTo, ""));
        setActivate(() -> targets.forEach(targetIdConsumer::accept));
        return this;
    }

    // Overrides targets
    public ActivatedAbility setTargetingForDamageDuringResolve(int count, boolean upTo, Consumer<UUID> targetIdConsumer) {
        setActivate(() -> {
            targets = game.selectDamageTargets(card.controller, count, upTo, "");
            targets.forEach(targetIdConsumer::accept);
        });
        return this;
    }


    // Overrides targets
    public ActivatedAbility setTargetingForDamage(Consumer<UUID> targetIdConsumer) {
        return setTargetingForDamage(1, false, targetIdConsumer);
    }

    // Overrides targets
    public ActivatedAbility setTargetingForDamageDuringResolve(Consumer<UUID> targetIdConsumer) {
        return setTargetingForDamageDuringResolve(1, false, targetIdConsumer);
    }

    // Overrides targets
    public ActivatedAbility setTargetingForSacrifice(Game.Zone zone, Predicate<Card> validCards, int count, boolean upTo, Consumer<UUID> targetIdConsumer) {
        if (!upTo) {
            addCanActivateAdditional(() -> game.hasIn(card.controller, zone, validCards, count));
        }
        addBeforeActivate(() -> {
            targets = game.selectFromZone(card.controller, zone, validCards, count, upTo, "Choose cards to sacrifice.");
            targets.forEach(targetId -> game.sacrifice(targetId));
        });
        setActivate(() -> targets.forEach(targetIdConsumer::accept));
        return this;
    }

    public ActivatedAbility setActivate(Runnable activate) {
        this.activate = activate;
        return this;
    }

    public Arraylist<UUID> getTargets() {
        return targets;
    }

    @SafeVarargs
    public final ActivatedAbility addCanActivateAdditional(Supplier<Boolean>... canActivateAdditionals) {
        canActivateAdditional.addAll(Arrays.asList(canActivateAdditionals));
        return this;
    }

    public ActivatedAbility setPurging() {
        this.purging = true;
        return this;
    }
}
