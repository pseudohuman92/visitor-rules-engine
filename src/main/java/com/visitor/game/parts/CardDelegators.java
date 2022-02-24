package com.visitor.game.parts;

import com.visitor.helpers.containers.ActivatedAbility;

import java.util.UUID;

public class CardDelegators extends Combat {
    /**
     * Adders
     */
    public void addTurnlyAttackAndHealth(UUID cardId, int attack, int health) {
        getCard(cardId).addTurnlyAttackAndHealth(attack, health);
    }

    public void addAttackAndHealth(UUID cardId, int attack, int health) {
        getCard(cardId).addAttackAndHealth(attack, health);
    }

    public void addTurnlyCombatAbility(UUID targetId, com.visitor.card.properties.Combat.CombatAbility combatAbility) {
        getCard(targetId).addTurnlyCombatAbility(combatAbility);
    }

    public void addTurnlyActivatedAbility(UUID cardId, ActivatedAbility ability) {
        getCard(cardId).addTurnlyActivatedAbility(ability);
    }

    public void addAttachmentTo(UUID attachedId, UUID attachmentId) {
        getCard(attachedId).addAttachment(attachmentId);
    }

    public void addCombatAbility(UUID cardId, com.visitor.card.properties.Combat.CombatAbility combatAbility) {
        getCard(cardId).addCombatAbility(combatAbility);
    }

    public void addShield(UUID cardId, int i) {
        getCard(cardId).addShield(i);
    }

    /**
     * Removers
     */
    public void removeAttachmentFrom(UUID attachedTo, UUID attachmentId) {
        getCard(attachedTo).removeAttachment(attachmentId);
    }

    public void removeAttackAndHealth(UUID cardId, int attack, int health) {
        getCard(cardId).loseAttack(attack);
        getCard(cardId).loseHealth(health);
    }

    /**
     * Getters
     */
    public int getAttack(UUID cardId) {
        return getCard(cardId).getAttack();
    }

    public int getHealth(UUID cardId) {
        return getCard(cardId).getHealth();
    }

    /**
     * Setters
     */
    public void setAttackAndHealth(UUID cardId, int attack, int health) {
        getCard(cardId).setAttack(attack);
        getCard(cardId).setHealth(health);
    }

    /**
     * Misc
     */
    public void heal(UUID cardId, int healAmount) {
        getCard(cardId).heal(healAmount);
    }
}
