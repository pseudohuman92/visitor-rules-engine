package com.visitor.game.parts;

import com.visitor.card.properties.Combat;
import com.visitor.game.Card;
import com.visitor.game.Event;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.UUIDHelper;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.helpers.containers.Damage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;
import static com.visitor.game.parts.GameBasePart.Zone.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class GameActionsPart extends GameCombatPart {
    public void addEnergy (UUID playerId, int i) {
        getPlayer(playerId).energy += i;
    }

    public void spendEnergy (UUID playerId, int i) {
        getPlayer(playerId).energy -= i;
    }

    public void draw (UUID playerId, int count) {
        com.visitor.game.Player player = getPlayer(playerId);
        player.draw(count);
        if (player.deck.isEmpty()) {
            gameEnd(playerId, false);
        }
    }

    public void draw (UUID playerId, UUID cardID) {
        getPlayer(playerId).hand.add(extractCard(cardID));
    }

    public void draw (UUID playerId, com.visitor.game.Card card) {
        getPlayer(playerId).hand.add(card);
    }

    public void purge (UUID playerId, UUID cardID) {
        getPlayer(playerId).voidPile.add(extractCard(cardID));
    }

    public void destroy (UUID targetId) {
        destroy(null, targetId);
    }

    public void destroy (UUID sourceId, UUID targetId) {
        com.visitor.game.Card c = getCard(targetId);
        if (sourceId != null) {
            addEvent(Event.destroy(getCard(sourceId), c));
        } else {
            addEvent(Event.destroy(null, c));
        }
        c.destroy();
    }

    public void loot (UUID playerId, int x) {
        this.draw(playerId, x);
        discard(playerId, x);
    }

    public void discard (UUID playerId, int count) {
        if (!getZone(playerId, Hand).isEmpty()) {
            String message = "Discard " + (count > 1 ? count + " cards." : "a card.");
            Arraylist<Card> d = getPlayer(playerId).discardAll(selectFromZone(playerId, Hand, Predicates::any, Math.min(count, getZone(playerId, Hand).size()), false, message));
            addEvent(Event.discard(playerId, d));
        }
    }

    public void discard (UUID playerId, UUID cardID) {
        getPlayer(playerId).discard(cardID);
        addEvent(Event.discard(playerId, getCard(cardID)));
    }

    public void discardAll (UUID playerId, Arraylist<com.visitor.game.Card> cards) {
        getPlayer(playerId).discardAll(UUIDHelper.toUUIDList(cards));
        addEvent(Event.discard(playerId, cards));
    }

    public void deplete (UUID id) {
        getCard(id).deplete();
    }

    public void ready (UUID id) {
        getCard(id).newTurn();
    }

    public void sacrifice (UUID cardId) {
        com.visitor.game.Card c = getCard(cardId);
        c.sacrifice();
        addEvent(Event.sacrifice(c));
    }

    public void donate (UUID donatedCardId, UUID newController, Zone newZone) {
        com.visitor.game.Card c = extractCard(donatedCardId);
        UUID oldController = c.controller;
        c.controller = newController;
        getZone(newController, newZone).add(c);
    }

    public void returnAllCardsToHand () {
        forEachInZone(UUID.randomUUID(), Both_Play, Predicates::any, this::returnToHand);
    }

    public void heal (UUID cardId, int healAmount) {
        getCard(cardId).heal(healAmount);
    }

    private com.visitor.game.Card createFreshCopy (com.visitor.game.Card c) {
        try {
            Class<?> cardClass = c.getClass();
            Constructor<?> cardConstructor = cardClass.getConstructor(Game.class, String.class);
            Object card = cardConstructor.newInstance(this, c.controller);
            return ((com.visitor.game.Card) card);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            getLogger(com.visitor.game.Deck.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

    public com.visitor.game.Card restore (UUID cardId) {
        return createFreshCopy(extractCard(cardId));
    }

    public void resurrect (UUID cardId) {
        HelperFunctions.runIfNotNull(getCard(cardId), () -> restore(cardId).resolve());
    }

    public void destroyAllUnits () {
        getZone(null, Both_Play).forEach(c -> {
            if (Predicates.isUnit(c)) {
                destroy(c.id);
            }
        });
    }

    public void cancel (UUID cardId) {
        com.visitor.game.Card card = extractCard(cardId);
        card.clear();
        putTo(card.controller, card, Discard_Pile);
    }

    public void purgeFromDeck (UUID playerId, int i) {
        getPlayer(playerId).purgeFromDeck(i);
    }

    public void addTurnlyActivatedAbility (UUID cardId, ActivatedAbility ability) {
        getCard(cardId).addTurnlyActivatedAbility(ability);
    }

    public com.visitor.game.Card discardAtRandom (UUID playerId) {
        return getPlayer(playerId).discardAtRandom();
    }

    public void purge (UUID cardId) {
        getCard(cardId).purge();
    }


    public void returnToHand (UUID cardId) {
        getCard(cardId).returnToHand();
    }

    public void gainControlFromZone (UUID newController, Zone oldZone, Zone newZone, UUID targetId) {
        runIfInZone(newController, oldZone, targetId,
                ()->{
                    com.visitor.game.Card c = extractCard(targetId);
                    c.controller = newController;
                    putTo(newController, c, newZone);
                });
    }

    public void removeMaxEnergy (UUID playerId, int count) {
        getPlayer(playerId).maxEnergy -= count;
    }

    public void payHealth (UUID playerId, int count) {
        getPlayer(playerId).payHealth(count);
    }

    public void possessTo (UUID newController, UUID cardID, Zone zone) {
        com.visitor.game.Card c = extractCard(cardID);
        UUID oldController = c.controller;
        c.controller = newController;
        getZone(newController, zone).add(c);
    }

    public void addStudyCount (UUID playerId, int count) {
        getPlayer(playerId).numOfStudiesLeft += count;
    }

    public void addAttachmentTo (UUID attachedId, UUID attachmentId) {
        getCard(attachedId).addAttachment(attachmentId);
    }

    public void removeAttachmentFrom (UUID attachedTo, UUID attachmentId) {
        getCard(attachedTo).removeAttachment(attachmentId);
    }

    public void removeAttackAndHealth (UUID cardId, int attack, int health) {
        getCard(cardId).loseAttack(attack);
        getCard(cardId).loseHealth(health);
    }

    public void gainHealth (UUID playerId, int health) {
        getPlayer(playerId).addHealth(health);
    }

    public void addCombatAbility (UUID cardId, Combat.CombatAbility combatAbility) {
        getCard(cardId).addCombatAbility(combatAbility);
    }

    public void addShield (UUID cardId, int i) {
        getCard(cardId).addShield(i);
    }

    public void fight (UUID cardId, UUID targetId) {
        com.visitor.game.Card c1 = getCard(cardId);
        com.visitor.game.Card c2 = getCard(targetId);

        dealDamage(c1.id, c2.id, new Damage(c1.getAttack(), false, false));
        dealDamage(c2.id, c1.id, new Damage(c2.getAttack(), false, false));

        if (c2.getHealth() == 0 || c1.hasCombatAbility(Deathtouch))
            destroy(c2.id);
        if (c1.getHealth() == 0 || c2.hasCombatAbility(Deathtouch))
            destroy(c1.id);
    }

}
