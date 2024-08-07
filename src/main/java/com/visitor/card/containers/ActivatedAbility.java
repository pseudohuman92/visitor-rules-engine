package com.visitor.card.containers;

import com.visitor.card.properties.Targetable;
import com.visitor.card.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.*;
import com.visitor.protocol.Types;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.visitor.game.parts.Base.Zone.*;

public class ActivatedAbility {

    private final Game game;
    public final UUID id;
    private final Card card;
    private int cost;
    private boolean depleting;
    private boolean slow;
    private boolean selfDestroying;
    private final String text;
    private CounterMap<Types.Knowledge> knowledgeRequirement;
    private final Arraylist<Supplier<Boolean>> canActivateAdditional;
    private final Hashmap<UUID, Effect> costEffects;
    private final Hashmap<UUID, Effect> effects;


    public ActivatedAbility(Game game, Card card, int cost, String text) {
        this.id = UUID.randomUUID();
        this.game = game;
        this.card = card;
        this.cost = cost;
        this.text = text;
        this.depleting = false;
        this.slow = false;
        this.selfDestroying = false;
        this.knowledgeRequirement = new CounterMap<>();
        this.canActivateAdditional = new Arraylist<>();
        this.costEffects = new Hashmap<>();
        this.effects = new Hashmap<>();
    }

    public ActivatedAbility(Game game, Card card, int cost, String text, Runnable effect) {
        this.id = UUID.randomUUID();
        this.game = game;
        this.card = card;
        this.cost = cost;
        this.text = text;
        this.depleting = false;
        this.slow = false;
        this.selfDestroying = false;
        this.knowledgeRequirement = new CounterMap<>();
        this.canActivateAdditional = new Arraylist<>();
        this.costEffects = new Hashmap<>();
        this.effects = new Hashmap<>();
        addTargeting(Play, Predicates::none, 0, 0, text, i -> effect.run(), false);
    }


    public final boolean canActivate() {
        boolean canActivate = game.hasKnowledge(card.controller, knowledgeRequirement) &&
                game.hasEnergy(card.controller, cost) &&
                (!depleting || !card.isDepleted()) &&
                ((!slow && game.canPlayFast(card.controller))|| game.canPlaySlow(card.controller));
        for (Supplier<Boolean> caa : canActivateAdditional) {
            canActivate = canActivate && caa.get();
        }
        for (Effect t : costEffects.values()){
            canActivate = canActivate && t.hasEnoughTargets();
        }
        for (Effect t : effects.values()){
            canActivate = canActivate && t.hasEnoughTargets();
        }
        return canActivate;
    }

    public final void activate() {
        for (Effect e : costEffects.values()) {
            e.runEffect();
        }
        game.removeEnergy(card.controller, cost);
        if (depleting) {
            game.deplete(card.getId());
        }
        if (selfDestroying) {
            game.destroy(card.getId());
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
        this.selfDestroying = true;
        return this;
    }

    public final void runEffects() {
        for (Effect t : effects.values()){
            t.runEffect();
        }
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

    // TODO: Refactor cards to use this.
    // Overrides targets
    public ActivatedAbility addTargeting(Game.Zone zone, Predicate<Targetable> predicate,
             int minCount, int maxCount, String text, Consumer<UUID> perTargetEffect, boolean forCost) {
        Effect targeting = new Effect(game, card, zone, minCount, maxCount, predicate, text, perTargetEffect);
        if (forCost){
            costEffects.put(targeting.getId(), targeting);
        } else {
            effects.put(targeting.getId(), targeting);
        }
        return this;
    }

    public ActivatedAbility addAbility(String text, Runnable effect, boolean forCost) {
        Effect targeting = new Effect(game, card, None, 0, 0, Predicates::none, text, t->{
            effect.run();
        });
        if (forCost){
            costEffects.put(targeting.getId(), targeting);
        } else {
            effects.put(targeting.getId(), targeting);
        }
        return this;
    }

    @SafeVarargs
    public final ActivatedAbility addCanActivateAdditional(Supplier<Boolean>... canActivateAdditionals) {
        canActivateAdditional.addAll(Arrays.asList(canActivateAdditionals));
        return this;
    }

    public Types.TargetingAbility getTargetingAbility(){
        Arraylist<Types.Targeting> a = new Arraylist<>();
        for (Effect t: costEffects.values()) {
            a.add(t.toTargetingBuilder().build());
        }
        for (Effect t: effects.values()) {
            a.add(t.toTargetingBuilder().build());
        }
        return Types.TargetingAbility.newBuilder().setId(id.toString()).addAllTargets(a).setText(text).build();
    }

    public void clear() {
        for (Effect t : costEffects.values()){
            t.clear();
        }
        for (Effect t : effects.values()){
            t.clear();
        }
    }

    public void setTargets(Arraylist<Types.TargetSelection> targets) {
        for (Types.TargetSelection t : targets) {
            Effect tr = costEffects.get(UUID.fromString(t.getId()));
            if (tr != null){
                tr.setTargets(UUIDHelper.toUUIDList(t.getTargetsList()));
            } else {
                tr = effects.get(UUID.fromString(t.getId()));
                if (tr != null){
                    tr.setTargets(UUIDHelper.toUUIDList(t.getTargetsList()));
                }
            }
        }
    }

    public Arraylist<UUID> getAllTargets() {
        Arraylist<UUID> targets = new Arraylist<>();
        for (Effect t : costEffects.values()){
            targets.addAll(t.getTargets());
        }
        for (Effect t : effects.values()){
            targets.addAll(t.getTargets());
        }
        return targets;
    }
}
