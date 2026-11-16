package com.visitor.card.containers;

import com.visitor.protocol.Types;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.visitor.game.IllegalGameActionException.parseId;
import static com.visitor.game.IllegalGameActionException.require;

/** Validate every group before callers store any targets or pay any costs. */
public final class TargetSelections {
    private TargetSelections() {}

    @SafeVarargs
    public static void validate(List<Types.TargetSelection> selections, Collection<Effect>... groups) {
        require(selections != null, "Target selections are required");
        Map<UUID, Effect> expected = new HashMap<>();
        for (Collection<Effect> group : groups) {
            for (Effect effect : group) {
                expected.put(effect.getId(), effect);
            }
        }
        Set<UUID> supplied = new HashSet<>();
        for (Types.TargetSelection selection : selections) {
            require(selection != null, "Invalid target selection");
            UUID id = parseId(selection.getId());
            Effect effect = expected.get(id);
            require(effect != null && supplied.add(id), "Unknown or repeated targeting group");
            effect.validateTargets(selection.getTargetsList());
        }
        for (Effect effect : expected.values()) {
            if (!supplied.contains(effect.getId())) {
                effect.validateTargets(Collections.emptyList());
            }
        }
    }
}
