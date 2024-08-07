package com.visitor.game.parts;

import com.visitor.card.properties.Damagable;
import com.visitor.card.Card;

import java.util.UUID;

public class CardDelegators extends Combat {
    /**
     * Adders
     */
    public void addAttackAndHealth(UUID cardId, int attack, int health, boolean turnly) {
        getCard(cardId).addAttackAndHealth(attack, health, turnly);
    }

    public void addAttachmentTo(UUID attachedId, UUID attachmentId) {
        getCard(attachedId).addAttachment(attachmentId);
    }

    public void addCombatAbility(UUID cardId, Damagable.CombatAbility combatAbility, boolean turnly) {
        getCard(cardId).addCombatAbility(combatAbility, turnly);
    }

    public void addShield(UUID id, int i, boolean turnly) {
        Card c = getCard(id);
        if (c != null)
            c.addShield(i, turnly);
        else {
            getPlayer(id).damagable.addShield(i, turnly);
        }
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
