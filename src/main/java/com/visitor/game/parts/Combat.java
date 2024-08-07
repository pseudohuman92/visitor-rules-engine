package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.helpers.Arraylist;
import com.visitor.card.containers.Damage;
import com.visitor.protocol.Types;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Predicate;

import static com.visitor.card.properties.Damagable.CombatAbility.FirstStrike;
import static com.visitor.game.parts.Base.Zone.Play;
import static java.lang.System.out;
import static java.util.UUID.fromString;

public class Combat extends Stack {

    // Does nothing if called with non-positive amount of damage
    public void dealDamage(UUID sourceId, UUID targetId, Damage damage) {
        if (damage.amount > 0) {
            Card source = getCard(sourceId);
            if (isPlayer(targetId)) {
                getPlayer(targetId).receiveDamage(damage, source);
            } else {
                Card c = getCard(targetId);
                if (c != null) {
                    c.receiveDamage(damage, source);
                }
            }
        }
    }

    public void dealDamage(UUID sourceId, UUID targetId, int damage) {
        dealDamage(sourceId, targetId, new Damage(damage));
    }

    public void dealDamageToAll(UUID playerId, UUID sourceId, Damage damage) {
        players.values().forEach(p -> dealDamage(sourceId, p.getId(), damage));
        getBothZones(Play).forEach(c -> dealDamage(sourceId, c.getId(), damage));
    }

    /**
     * Damagable Helper Methods
    */



    void dealCombatDamage() {
        ArrayList<Card> firstStrikeAttackers = new Arraylist<>(getAllCardsById(attackers));
        firstStrikeAttackers.removeIf(a -> !a.hasCombatAbility(FirstStrike));
        firstStrikeAttackers.forEach(a -> a.dealAttackDamage(true));

        processEvents();

        ArrayList<Card> normalAttackers = new Arraylist<>(getAllCardsById(attackers));
        normalAttackers.removeIf(a -> a.hasCombatAbility(FirstStrike));
        ArrayList<Card> normalBlockers = new Arraylist<>(getAllCardsById(blockers));

        normalAttackers.forEach(a -> a.dealAttackDamage(false));
        normalBlockers.forEach(Card::dealBlockDamage);

        //TODO: these kill if they blocked by a deathtouch unit even if it is not damaged by it.
        normalAttackers.forEach(Card::maybeDieFromBlock);
        normalBlockers.forEach(Card::maybeDieFromAttack);

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


    public void assignDamage(UUID id, Arraylist<UUID> possibleTargets, Damage damage, boolean trample) {
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

    public Arraylist<Card> getPossibleBlockTargets(Predicate<Card> pred) {
        Arraylist<Card> targets = new Arraylist<>();
        attackers.forEach(i -> {
            if (pred.test(getCard(i))){
                targets.add(getCard(i));
            }
        });
        return targets;
    }
}
