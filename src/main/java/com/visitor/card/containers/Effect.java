package com.visitor.card.containers;

import com.visitor.card.properties.Targetable;
import com.visitor.card.Card;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Effect {

    private final Game game;
    private final Card card;
    private final UUID id;
    private final int minTargets;
    private final int maxTargets;
    private final Predicate<Targetable> predicate;
    private final String targetingMessage;
    private final Base.Zone zone;
    private Arraylist<UUID> targets;

    private final Hashmap<UUID, Base.Zone> targetZones;
    private final Consumer<UUID> targetsEffect;

    public Effect(
            Game game,
            Card card,
            Base.Zone zone,
            int minTargets,
            int maxTargets,
            Predicate<Targetable> predicate,
            String targetingMessage,
            Consumer<UUID> targetsEffect) {
        this.game = game;
        this.card = card;
        this.zone = Objects.requireNonNullElse(zone, Base.Zone.None);
        this.id = UUID.randomUUID();
        this.minTargets = minTargets;
        this.maxTargets = maxTargets;
        this.predicate = predicate != null ? predicate : Predicates::none;
        this.targetingMessage = targetingMessage != null ? targetingMessage : "";
        this.targetsEffect = targetsEffect;
        this.targets = new Arraylist<>();
        this.targetZones = new Hashmap<>();
    }

    public Effect(
            Game game,
            Card card,
            Runnable effect) {
        this(game, card, Base.Zone.Play, 0, 0, Predicates::none, "", i -> effect.run());
    }

    public int getMinTargets() {
        return minTargets;
    }

    public int getMaxTargets() {
        return maxTargets;
    }

    public String getTargetingMessage() {
        return targetingMessage;
    }

    public Arraylist<UUID> getAllPossibleTargets(){return new Arraylist<>(game.getAllFrom(card.controller, zone, predicate).transform(Targetable::getId)); }


    public Types.Targeting.Builder toTargetingBuilder() {
        return Types.Targeting.newBuilder()
                .setId(id.toString())
                .setMinTargets(minTargets)
                .setMaxTargets(maxTargets)
                .setTargetMessage(targetingMessage)
                .addAllPossibleTargets(getAllPossibleTargets().transformToStringList())
                .setTargetingZone(HelperFunctions.zoneToSelectFromType(zone));
    }

    public Arraylist<UUID> getTargets() {
        return targets;
    }

    public void setTargets(Arraylist<UUID> targets) {
        this.targets = targets;
        targets.forEach(i -> targetZones.put(i, game.getTargetable(i).getZone()));
    }

    public UUID getId() {
        return id;
    }

    public void clear() {
        targets.clear();
        targetZones.clear();
    }

    public void runEffect(){
        if (maxTargets < 1) {
            targetsEffect.accept(UUID.randomUUID());
        } else {
            for (int i = 0; i < targets.size(); i++) {
                UUID t = targets.get(i);
                if (game.getTargetable(t).getZone() == targetZones.get(t))
                    targetsEffect.accept(t);
            }
        }
        targets.clear();
    }

    public Base.Zone getZone() {
        return zone;
    }

    public boolean hasEnoughTargets(){
        return game.hasInWithPlayers(card.controller, zone, predicate, minTargets);
    }
}
