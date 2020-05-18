package com.visitor.game;

import com.visitor.card.types.Card;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;
import com.visitor.protocol.Types.KnowledgeGroup;

import java.io.Serializable;
import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Lifelink;
import static java.util.UUID.randomUUID;

/**
 * @author pseudo
 */
public class Player implements Serializable {

    public final Game game;

    public String username;
    public UUID id;
    public int energy;
    public int maxEnergy;
    public int numOfStudiesLeft;
    public Deck deck;
    public Arraylist<Card> hand;
    public Arraylist<Card> scrapyard;
    public Arraylist<Card> voidPile;
    public Arraylist<Card> playArea;
    public Hashmap<Knowledge, Integer> knowledgePool;
    public int health;
    public int shield;

    /**
     * @param username
     */
    public Player(Game game, String username, String[] decklist) {
        this.game = game;
        this.username = username;
        id = randomUUID();
        this.deck = new Deck(game, username, decklist);
        energy = 0;
        maxEnergy = 0;
        numOfStudiesLeft = 1;
        hand = new Arraylist<>();
        scrapyard = new Arraylist<>();
        voidPile = new Arraylist<>();
        playArea = new Arraylist<>();
        knowledgePool = new Hashmap<>();
        health = 30;
        shield = 0;
    }

    public void draw(int count) {
        hand.addAll(deck.extractFromTop(count));
    }

    public void receiveDamage(int damageAmount, Card source) {
        int damage = damageAmount;

        // Apply shield
        if (shield >= damage) {
            shield -= damage;
            return;
        }
        damage -= shield;
        shield = 0;

        health -= damage;
        if (source.combat.combatAbilittList.contains(Lifelink)){
            game.gainHealth(source.controller, damage);
        }
        if (health <= 0) {
            game.gameEnd(username, false);
        }
    }

    public void payLife(int damage) {
        health -= damage;
    }

    public Card discard(UUID cardId) {
        Card c = extractCardFromList(cardId, hand);
        scrapyard.add(c);
        return c;
    }

    public Arraylist<Card> discardAll(Arraylist<UUID> cardIds) {
        Arraylist<Card> discarded = new Arraylist<>();
        cardIds.stream().map((cardID) -> extractCardFromList(cardID, hand))
                .forEachOrdered((card) -> {
                    discarded.add(card);
                    scrapyard.add(card);
                });
        return discarded;
    }

    public void redraw() {
        int size = hand.size();
        if (size > 0) {
            deck.addAll(hand);
            hand.clear();
            deck.shuffle();
            draw(size - 1);
        }
    }

    public void newTurn() {
        energy = maxEnergy;
        numOfStudiesLeft = 1;
        playArea.forEach((card) -> card.ready());
        resetShields();
    }

    public void resetShields() {
        playArea.forEach((card) -> {
            if (card.combat != null)
                card.combat.resetShields();
        });
    }

    public void addKnowledge(Hashmap<Knowledge, Integer> knowledge) {
        knowledge.forEach((k, i) -> knowledgePool.merge(k, i, Integer::sum));

    }

    public boolean hasKnowledge(Hashmap<Knowledge, Integer> cardKnowledge) {
        boolean result = true;
        for (Knowledge k : cardKnowledge.keySet()) {
            result = result && cardKnowledge.get(k) <= knowledgePool.getOrDefault(k, 0);
        }
        return result;
    }

    public Card extractCardFromList(UUID cardID, Arraylist<Card> list) {
        if (cardID == null) {
            System.out.println("CardID is NULL!");
        }
        for (Card card : list) {
            if (card == null) {
                System.out.println("Card is NULL!");
            }
            if (card.id.equals(cardID)) {
                list.remove(card);
                return card;
            }
        }
        return null;
    }

    public Card extractCard(UUID cardID) {
        Card c;
        Arraylist<Arraylist<Card>> lists = new Arraylist<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard);
        lists.add(voidPile);
        lists.add(deck);
        for (Arraylist<Card> list : lists) {
            c = extractCardFromList(cardID, list);
            if (c != null) {
                return c;
            }
        }
        return null;
    }

    public Card getCardFromList (UUID cardID, Arraylist<Card> list) {
        for (Card card : list) {
            if (card.id.equals(cardID)) {
                return card;
            }
        }
        return null;
    }

    public Card getCard(UUID cardID) {
        Card c;
        Arraylist<Arraylist<Card>> lists = new Arraylist<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard);
        lists.add(voidPile);
        lists.add(deck);
        for (Arraylist<Card> list : lists) {
            c = getCardFromList(cardID, list);
            if (c != null) {
                return c;
            }
        }
        return null;
    }

    void replaceCardWith(Card oldCard, Card newCard) {
        Arraylist<Arraylist<Card>> lists = new Arraylist<>();
        lists.add(hand);
        lists.add(playArea);
        lists.add(scrapyard);
        lists.add(voidPile);
        lists.add(deck);
        for (Arraylist<Card> list : lists) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(oldCard)) {
                    list.remove(i);
                    list.add(i, newCard);
                }
            }
        }
    }

    public Types.Player.Builder toPlayerMessage(boolean player) {
        Types.Player.Builder builder = Types.Player.newBuilder()
                .setId(id.toString())
                .setUserId(username)
                .setDeckSize(deck.size())
                .setEnergy(energy)
                .setMaxEnergy(maxEnergy)
                .setShield(shield)
                .setHandSize(hand.size())
                .setHealth(health)
                .addAllPlay(playArea.transform(c -> c.toCardMessage().build()))
                .addAllScrapyard(scrapyard.transform(c -> c.toCardMessage().build()))
                .addAllVoid(voidPile.transform(c -> c.toCardMessage().build()));

        if (player){
            builder.addAllHand(hand.transform(c -> c.toCardMessage().build()));
        }

        knowledgePool.forEach((k, i) -> builder.addKnowledgePool(KnowledgeGroup.newBuilder()
                .setKnowledge(k)
                .setCount(i).build()));
        return builder;
    }




}
