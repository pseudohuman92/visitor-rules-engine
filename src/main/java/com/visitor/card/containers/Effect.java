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
import java.util.HashMap;
import java.util.UUID;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.visitor.game.IllegalGameActionException.parseId;
import static com.visitor.game.IllegalGameActionException.require;

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
        targetZones.clear();
        targets.forEach(i -> targetZones.put(i, game.getTargetable(i).getZone()));
    }

    public void validateTargets(List<String> selected) {
        require(selected.size() >= minTargets && selected.size() <= maxTargets,
                "Wrong number of targets");
        Set<UUID> possible = new HashSet<>(getAllPossibleTargets());
        Set<UUID> unique = new HashSet<>();
        for (String value : selected) {
            UUID target = parseId(value);
            require(unique.add(target) && possible.contains(target), "Illegal or repeated target");
        }
    }

    public UUID getId() {
        return id;
    }

    public void clear() {
        targets.clear();
        targetZones.clear();
    }

    public void runEffect(){
        runEffect(targets, targetZones);
        clear();
    }

    /** Captures this activation's selections for an independently resolving stack entry. */
    public Runnable snapshot() {
        Arraylist<UUID> selectedTargets = new Arraylist<>(targets);
        Map<UUID, Base.Zone> selectedZones = new HashMap<>(targetZones);
        return () -> runEffect(selectedTargets, selectedZones);
    }

    private void runEffect(Arraylist<UUID> selectedTargets, Map<UUID, Base.Zone> selectedZones) {
        if (maxTargets < 1) {
            targetsEffect.accept(UUID.randomUUID());
        } else {
            for (int i = 0; i < selectedTargets.size(); i++) {
                UUID t = selectedTargets.get(i);
                Targetable target = game.getTargetable(t);
                if (target != null && target.getZone() == selectedZones.get(t))
                    targetsEffect.accept(t);
            }
        }
    }

    public Base.Zone getZone() {
        return zone;
    }

    public boolean hasEnoughTargets(){
        return getAllPossibleTargets().size() >= minTargets;
    }
}
