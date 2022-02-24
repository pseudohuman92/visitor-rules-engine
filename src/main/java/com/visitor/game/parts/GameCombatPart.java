package com.visitor.game.parts;

import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.containers.Damage;
import com.visitor.protocol.Types;

import java.util.ArrayList;
import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.FirstStrike;
import static com.visitor.game.parts.GameBasePart.Zone.Both_Play;
import static java.lang.System.out;
import static java.util.UUID.fromString;

public class GameCombatPart extends GameStackPart {
    // Does nothing if called with non-positive amount of damage
    public void dealDamage (UUID sourceId, UUID targetId, Damage damage) {
        if (damage.amount > 0) {
            com.visitor.game.Card source = getCard(sourceId);
            if (isPlayer(targetId)) {
                getPlayer(targetId).receiveDamage(damage.amount, source);
            } else {
                com.visitor.game.Card c = getCard(targetId);
                if (c != null) {
                    c.receiveDamage(damage, source);

                }
            }
            source.combat.triggerDamageEffects(targetId, damage);
        }
    }

    public void dealDamage (UUID sourceId, UUID targetId, int damage) {
        dealDamage(sourceId, targetId, new Damage(damage));
    }

    public void dealDamageToAll (UUID playerId, UUID sourceId, Damage damage) {
        players.values().forEach(p -> dealDamage(sourceId, p.id, damage));
        getZone(playerId, Both_Play).forEach(c -> dealDamage(sourceId, c.id, damage));
    }

    /**
     * Combat Helper Methods
     */
    void chooseAttackers() {
        out.println("Updating players from chooseAttackers. AP: " + activePlayer);
        updatePlayers();
        Arraylist<Types.AttackerAssignment> attackerIds = selectAttackers(turnPlayer);
        out.println("Attackers: " + attackerIds);
        Arraylist<com.visitor.game.Card> attackingCards = new Arraylist<>();
        attackerIds.forEach(c -> {
            com.visitor.game.Card u = getCard(fromString(c.getAttackerId()));
            u.setAttacking(fromString(c.getAttacksTo()));
            attackers.add(u.id);
            attackingCards.add(u);
        });
        addEvent(Event.attack(attackingCards), true);
    }

    void chooseBlockers() {
        out.println("Updating players from chooseBlockers. AP: " + activePlayer);
        updatePlayers();
        Arraylist<Types.BlockerAssignment> assignedBlockers = selectBlockers(this.getOpponentId(turnPlayer));
        out.println("Blockers: " + assignedBlockers);
        assignedBlockers.forEach(c -> {
            UUID blockerId = fromString(c.getBlockerId());
            UUID blockedBy = fromString(c.getBlockedBy());

            getCard(blockedBy).addBlocker(blockerId);
            com.visitor.game.Card blocker = getCard(blockerId);
            blocker.setBlocking(blockedBy);
            blockers.add(blockerId);
        });

    }

    void dealCombatDamage() {
        ArrayList<Card> firstStrikeAttackers = new Arraylist<>(getAll(attackers));
        firstStrikeAttackers.removeIf(a -> !a.hasCombatAbility(FirstStrike));
        firstStrikeAttackers.forEach(a -> a.dealAttackDamage(true));

        processEvents();

        ArrayList<com.visitor.game.Card> normalAttackers = new Arraylist<>(getAll(attackers));
        normalAttackers.removeIf(a -> a.hasCombatAbility(FirstStrike));
        ArrayList<com.visitor.game.Card> normalBlockers = new Arraylist<>(getAll(blockers));

        normalAttackers.forEach(a -> a.dealAttackDamage(false));
        normalBlockers.forEach(com.visitor.game.Card::dealBlockDamage);

        //TODO: these kill if they blocked by a deathtouch unit even if it is not damaged by it.
        normalAttackers.forEach(com.visitor.game.Card::maybeDieFromBlock);
        normalBlockers.forEach(com.visitor.game.Card::maybeDieFromAttack);

        processEvents();
    }

    void unsetAttackers() {
        attackers.forEach(u -> getCard(u).unsetAttacking());
        attackers.clear();
    }

    void unsetBlockers() {
        blockers.forEach(u -> getCard(u).unsetBlocking());
        blockers.clear();
    }


    public void assignDamage (UUID id, Arraylist<UUID> possibleTargets, Damage damage, boolean trample) {
        out.println("Updating players from assignDamage. AP: " + activePlayer);
        updatePlayers();
        Arraylist<Types.DamageAssignment> assignedDamages = assignDamage(turnPlayer, id, possibleTargets, damage.amount, trample);
        out.println("Damage distribution: " + assignedDamages);
        assignedDamages.forEach(c -> {
            UUID targetId = fromString(c.getTargetId());
            int assignedDamage = c.getDamage();
            dealDamage(id, targetId, new Damage(assignedDamage, damage.mayKill, damage.combat));
        });
    }
}
