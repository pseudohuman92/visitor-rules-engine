package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.card.properties.Targetable;
import com.visitor.game.Event;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.AttackerAssignment;
import com.visitor.protocol.Types.BlockerAssignment;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.visitor.game.IllegalGameActionException.parseId;
import static com.visitor.game.IllegalGameActionException.require;
import static com.visitor.protocol.Types.Phase.*;

public class ClientActions extends Turns {
    private void requirePlayer(UUID playerId) {
        require(playerId != null && players.containsKey(playerId), "Unknown player");
    }

    private void requireActivePlayer(UUID playerId) {
        requirePlayer(playerId);
        require(playerId.equals(activePlayer), "Player does not have initiative");
    }

    private Card requireOwnedCard(UUID playerId, UUID cardId, Zone zone) {
        requirePlayer(playerId);
        Card card = getCard(cardId);
        require(card != null && playerId.equals(card.controller) && card.zone == zone
                && getZone(playerId, zone).contains(card), "Card is not controlled by this player in " + zone);
        return card;
    }

    /** withCost=false is reserved for trusted card effects, not a wire-message option. */
    public void playCard(UUID playerId, UUID cardId, boolean withCost, Arraylist<Types.TargetSelection> targets) {
        if (withCost) {
            requireActivePlayer(playerId);
        }
        Card card = requireOwnedCard(playerId, cardId, Zone.Hand);
        require(card.canPlay(withCost), "Card cannot be played now");
        card.validatePlayableTargets(targets);
        // All command validation precedes target storage, extraction, costs and events.
        card.setPlayableTargets(targets);
        extractCard(cardId);
        card.play(withCost);
        addEvent(Event.playCard(card), true);
        activePlayer = getOpponentId(playerId);
    }

    public void playCard(UUID playerId, UUID cardId, Arraylist<Types.TargetSelection> targets) {
        playCard(playerId, cardId, true, targets);
    }

    public void playCardWithoutCost(UUID playerId, UUID cardId, Arraylist<Types.TargetSelection> targets) {
        playCard(playerId, cardId, false, targets);
    }

    public void activateCard(UUID playerId, UUID cardId, UUID abilityId, Arraylist<Types.TargetSelection> targets) {
        requireActivePlayer(playerId);
        Card card = requireOwnedCard(playerId, cardId, Zone.Play);
        require(card.canActivate(abilityId), "Selected ability cannot be activated now");
        card.validateActivatableTargets(abilityId, targets);
        card.setActivatableTargets(abilityId, targets);
        card.activate(abilityId);
        activePlayer = getOpponentId(playerId);
    }

    /** Non-regular studies are reserved for trusted rule effects. */
    public void studyCard(UUID playerId, UUID cardId, boolean regular, CounterMap<Types.Knowledge> knowledge) {
        if (regular) {
            requireActivePlayer(playerId);
        }
        Card card = requireOwnedCard(playerId, cardId, Zone.Hand);
        require(knowledge != null && card.isStudiable() && (!regular || card.canStudy()),
                "Card cannot be studied now");
        if (regular) {
            if (card.knowledge.isEmpty()) {
                require(knowledge.isEmpty(), "This card does not offer a knowledge choice");
            } else {
                require(knowledge.size() == 1, "Choose one knowledge offered by the card");
                Types.Knowledge selected = knowledge.keySet().iterator().next();
                require(selected != null && card.knowledge.getOrDefault(selected, 0) > 0
                        && Integer.valueOf(1).equals(knowledge.get(selected)), "Invalid study knowledge");
            }
        }
        //extractCard(cardId);
        card.study(getPlayer(playerId), regular, knowledge);
        addEvent(Event.study(playerId, card), regular);
    }

    public void pass(UUID playerId) {
        requireActivePlayer(playerId);
        require(canPlayFast(playerId), "Cannot pass in this phase");
        passCount++;
        if (passCount == 2) {
            if (!stack.isEmpty()) {
                resolveStack();
            } else {
                changePhase();
            }
        } else {
            activePlayer = getOpponentId(playerId);
        }
    }

    public void redraw(UUID playerId) {
        requireActivePlayer(playerId);
        require(phase == REDRAW, "Redraw is only allowed during opening-hand selection");
        getPlayer(playerId).redraw();
    }

    public void keep(UUID playerId) {
        requireActivePlayer(playerId);
        require(phase == REDRAW, "Keep is only allowed during opening-hand selection");
        passCount++;
        if (passCount == 2) {
            changePhase();
        } else {
            activePlayer = getOpponentId(playerId);
        }
    }

    public void selectAttackers(UUID playerId, AttackerAssignment[] assignments) {
        requireActivePlayer(playerId);
        require(canAttack(playerId) && assignments != null, "Cannot declare attackers now");
        Map<Card, UUID> declared = new LinkedHashMap<>();
        for (AttackerAssignment assignment : assignments) {
            require(assignment != null, "Invalid attacker assignment");
            Card attacker = requireOwnedCard(playerId, parseId(assignment.getAttackerId()), Zone.Play);
            UUID targetId = parseId(assignment.getAttacksTo());
            Targetable target = getTargetable(targetId);
            boolean enemyTarget = getOpponentId(playerId).equals(targetId)
                    || (target instanceof Card && target.getZone() == Zone.Play && target.isDamagable()
                    && getOpponentId(playerId).equals(((Card) target).controller));
            require(attacker.canAttack() && !declared.containsKey(attacker) && enemyTarget,
                    "Illegal or repeated attacker/attack target");
            declared.put(attacker, targetId);
        }
        Arraylist<Card> attackingCards = new Arraylist<>();
        declared.forEach((card, target) -> {
            card.setAttacking(target);
            attackers.add(card.getId());
            attackingCards.add(card);
        });
        addEvent(Event.attack(attackingCards), true);
        changePhase();
    }

    public void selectBlockers(UUID playerId, BlockerAssignment[] assignments) {
        requireActivePlayer(playerId);
        require(canBlock(playerId) && assignments != null, "Cannot declare blockers now");
        Map<Card, Card> declared = new LinkedHashMap<>();
        Set<UUID> blockedAttackers = new HashSet<>();
        for (BlockerAssignment assignment : assignments) {
            require(assignment != null, "Invalid blocker assignment");
            Card blocker = requireOwnedCard(playerId, parseId(assignment.getBlockerId()), Zone.Play);
            UUID attackerId = parseId(assignment.getBlockedBy());
            Card attacker = getCard(attackerId);
            require(attacker != null && attacker.zone == Zone.Play && attackers.contains(attackerId)
                    && getOpponentId(playerId).equals(attacker.controller) && attacker.isAttacking(),
                    "Block target is not an attacking enemy");
            require(blocker.canBlock() && blocker.canBlock(attacker) && !declared.containsKey(blocker)
                    && blockedAttackers.add(attackerId), "Illegal or repeated blocker assignment");
            declared.put(blocker, attacker);
        }
        declared.forEach((blocker, attacker) -> {
            attacker.addBlocker(blocker.getId());
            blocker.setBlocking(attacker.getId());
            blockers.add(blocker.getId());
        });
        changePhase();
    }
}
