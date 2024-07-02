package com.visitor.game.parts;

import com.visitor.card.properties.Targetable;
import com.visitor.card.Card;
import com.visitor.game.Event;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.UUIDHelper;
import com.visitor.card.containers.Damage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Deadly;
import static com.visitor.game.parts.Base.Zone.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class Actions extends PlayerDelegators {

    public Arraylist<UUID> draw(UUID playerId, int count) {
        com.visitor.game.Player player = getPlayer(playerId);
        Arraylist<UUID> drawn = player.draw(count);
        if (player.deck.isEmpty()) {
            gameEnd(playerId, false);
        }
        return drawn;
    }

    public void draw(UUID playerId, UUID cardID) {
        if (getCard(cardID).zone == Deck) {
            Card c = extractCard(cardID);
            c.zone = Hand;
            getPlayer(playerId).hand.add(c);
        }
    }

    public void draw(UUID playerId, Card card) {
        card.zone = Hand;
        getPlayer(playerId).hand.add(card);
    }

    public void destroy(UUID targetId) {
        Card c = getCard(targetId);
        addEvent(Event.death(c));
        c.destroy();
    }

    public void loot(UUID playerId, int x) {
        this.draw(playerId, x);
        discard(playerId, x);
    }

    public void discard(UUID playerId, int count) {
        if (!getZone(playerId, Hand).isEmpty()) {
            String message = "Discard " + (count > 1 ? count + " cards." : "a card.");
            Arraylist<Card> d = getPlayer(playerId).discardAll(selectFromZone(playerId, Hand, Predicates::any, Math.min(count, getZone(playerId, Hand).size()), Math.min(count, getZone(playerId, Hand).size()), message));
            addEvent(Event.discard(playerId, d));
        }
    }

    public void discard(UUID playerId, UUID cardID) {
        if (getCard(cardID).zone == Hand) {
            getPlayer(playerId).discard(cardID);
            addEvent(Event.discard(playerId, getCard(cardID)));
        }
    }

    public void discardAll(UUID playerId, Arraylist<Card> cards) {
        getPlayer(playerId).discardAll(UUIDHelper.toUUIDList(cards));
        addEvent(Event.discard(playerId, cards));
    }

    public void deplete(UUID id) {
        getCard(id).deplete();
    }

    public void ready(UUID id) {
        getCard(id).newTurn();
    }

    public void donate(UUID donatedCardId, UUID newController, Zone newZone) {
        Card c = extractCard(donatedCardId);
        UUID oldController = c.controller;
        c.controller = newController;
        c.zone = newZone;
        ((Arraylist<Card>) getZone(newController, newZone)).add(c);
    }

    public void returnAllCardsToHand() {
        forEachInZone(UUID.randomUUID(), Both_Play, Predicates::any, this::returnToHand);
    }

    private Card createFreshCopy(Card c) {
        try {
            Class<?> cardClass = c.getClass();
            Constructor<?> cardConstructor = cardClass.getConstructor(Game.class, String.class);
            Object card = cardConstructor.newInstance(this, c.controller);
            return ((Card) card);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            getLogger(com.visitor.game.Deck.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

    public Card restore(UUID cardId) {
        return createFreshCopy(extractCard(cardId));
    }

    public void resurrect(UUID cardId) {
        HelperFunctions.runIfNotNull(getCard(cardId), () -> restore(cardId).resolve());
    }

    public void destroyAllUnits() {
        getZone(null, Both_Play).forEach(c -> {
            if (Predicates.isUnit(c)) {
                destroy(c.getId());
            }
        });
    }

    public void cancel(UUID cardId) {
        Card card = extractCard(cardId);
        card.clear();
        putTo(card.controller, card, Discard_Pile);
    }

    public void purgeFromDeck(UUID playerId, int i) {
        getPlayer(playerId).purgeFromDeck(i);
    }


    public Card extractAtRandom(UUID playerId, Arraylist<Card> list) {
        return getPlayer(playerId).extractAtRandom(list);
    }

    public void returnToHand(UUID cardId) {
        Card c = getCard(cardId);
        if (c.zone == Play)
            c.returnToHand();
    }

    public void gainControlFromZone(UUID newController, Zone oldZone, Zone newZone, UUID targetId) {
        runIfInZone(newController, oldZone, targetId,
                () -> {
                    Card c = extractCard(targetId);
                    c.controller = newController;
                    putTo(newController, c, newZone);
                });
    }

    public void possessTo(UUID newController, UUID cardID, Zone zone) {
        Card c = extractCard(cardID);
        UUID oldController = c.controller;
        c.controller = newController;
        ((Arraylist<Targetable>)getZone(newController, zone)).add(c);
    }

    public void fight(UUID cardId, UUID targetId) {
        Card c1 = getCard(cardId);
        Card c2 = getCard(targetId);

        if (c1.zone == Play && c2.zone == Play) {
            dealDamage(c1.getId(), c2.getId(), new Damage(c1.getAttack(), false, false));
            dealDamage(c2.getId(), c1.getId(), new Damage(c2.getAttack(), false, false));

            if (c2.getHealth() == 0 || c1.hasCombatAbility(Deadly))
                destroy(c2.getId());
            if (c1.getHealth() == 0 || c2.hasCombatAbility(Deadly))
                destroy(c1.getId());
        }
    }

    public Card discardAtRandom(UUID controller) {
        Card c = getPlayer(controller).extractAtRandom(getPlayer(controller).hand);
        c.zone = Discard_Pile;
        getPlayer(controller).discardPile.add(c);
        return c;
    }

    public void drain(UUID sourceId, UUID targetId, int amount) {
        if (getCard(targetId).zone == Play) {
            int drained = getCard(targetId).drain(amount);
            getPlayer(sourceId).addHealth(drained);
        }
    }
}
