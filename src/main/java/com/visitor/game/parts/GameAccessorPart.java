package com.visitor.game.parts;

import com.visitor.game.Card;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.visitor.game.parts.GameBasePart.Zone.Both_Play;
import static com.visitor.protocol.Types.Phase.MAIN_AFTER;
import static com.visitor.protocol.Types.Phase.MAIN_BEFORE;
import static com.visitor.protocol.Types.SelectFromType.NOTYPE;

public class GameAccessorPart extends GameConnectionPart {
    /**
     * Card Accessor Methods
     * Getting Card objects from various places
     */
    Arraylist<Card> getZone(UUID playerId, Game.Zone zone) {
        switch (zone) {
            case Deck:
                return getPlayer(playerId).deck;
            case Hand:
                return getPlayer(playerId).hand;
            case Play:
                return getPlayer(playerId).playArea;
            case Discard_Pile:
                return getPlayer(playerId).discardPile;
            case Void:
                return getPlayer(playerId).voidPile;
            case Stack:
                return stack;
            case Opponent_Play:
                return getPlayer(getOpponentId(playerId)).playArea;
            case Opponent_Hand:
                return getPlayer(getOpponentId(playerId)).hand;
            case Opponent_Discard_Pile:
                return getPlayer(getOpponentId(playerId)).discardPile;
            case Both_Play:
                Arraylist<Card> total = new Arraylist<>();
                players.values().forEach(player -> total.addAll(player.playArea));
                return total;
            default:
                return null;
        }
    }

    Types.SelectFromType getZoneLabel(Game.Zone zone) {
        switch (zone) {
            case Hand:
                return Types.SelectFromType.HAND;
            case Play:
            case Both_Play:
            case Opponent_Play:
                return Types.SelectFromType.PLAY;
            case Discard_Pile:
            case Opponent_Discard_Pile:
                return Types.SelectFromType.DISCARD_PILE;
            case Void:
                return Types.SelectFromType.VOID;
            case Stack:
                return Types.SelectFromType.STACK;
            case Deck:
            case Opponent_Hand:
                return Types.SelectFromType.LIST;
            default:
                return NOTYPE;
        }
    }

    Player getPlayer(UUID playerId) {
        return players.get(playerId);
    }

    public int getPlayerEnergy (UUID playerId) {
        return getPlayer(playerId).energy;
    }

    public UUID getPlayerId (String username) {
        for (Player p : players.values()){
            if (p.username.equals(username)) return p.id;
        }
        return null;
    }

    public UUID getOpponentId (UUID playerId) {
        for (UUID id : players.keySet()) {
            if (!id.equals(playerId)) {
                return id;
            }
        }
        return null;
    }

    public Card getCard (UUID targetID) {
        for (Player player : players.values()) {
            Card c = player.getCard(targetID);
            if (c != null) {
                return c;
            }
        }
        for (Card c : stack) {
            if (c.id.equals(targetID)) {
                return c;
            }
        }
        return null;
    }

    private Card getCardFromZone (UUID playerId, Game.Zone zone, UUID cardId) {
        Arraylist<Card> card = new Arraylist<>();
        getZone(playerId, zone).forEach(c -> {
            if (c.id.equals(cardId))
                card.add(c);
        });
        return card.size() > 0 ? card.get(0) : null;
    }

    public Arraylist<Card> getAllFrom (UUID playerId, Game.Zone zone, Predicate<Card> pred) {
        Arraylist<Card> cards = new Arraylist<>();
        getZone(playerId, zone).forEach(c -> {
            if (pred.test(c)) {
                cards.add(c);
            }
        });
        return cards;
    }

    Arraylist<Card> getAll(List<UUID> list) {
        return new Arraylist<>(list.stream().map(this::getCard).collect(Collectors.toList()));
    }

    public Card extractCard (UUID targetID) {
        for (Player player : players.values()) {
            Card c = player.extractCard(targetID);
            if (c != null) {
                return c;
            }
        }
        for (Card c : stack) {
            if (c.id.equals(targetID)) {
                stack.remove(c);
                return c;
            }
        }
        return null;
    }

    private Arraylist<Card> extractAll (List<UUID> list) {
        return new Arraylist<>(list.stream().map(this::extractCard).collect(Collectors.toList()));
    }

    private Arraylist<Card> extractAllCopiesFrom (UUID playerId, String cardName, Game.Zone zone) {
        Arraylist<Card> cards = new Arraylist<>(getZone(playerId, zone).parallelStream()
                .filter(c -> c.name.equals(cardName)).collect(Collectors.toList()));
        getZone(playerId, zone).removeAll(cards);
        return cards;
    }

    public Arraylist<com.visitor.game.Card> extractFromTopOfDeck (UUID playerId, int count) {
        return getPlayer(playerId).extractFromTopOfDeck(count);
    }

    public com.visitor.game.Card extractTopmostMatchingFromDeck (UUID playerId, Predicate<com.visitor.game.Card> cardPredicate) {
        return getPlayer(playerId).extractTopmostMatchingFromDeck(cardPredicate);
    }

    public UUID getId () {
        return id;
    }

    public boolean isPlayerActive (UUID playerId) {
        return activePlayer.equals(playerId);
    }

    public boolean isPlayerInGame (String username) {
        return getPlayerId(username) != null;
    }

    public boolean isTurnPlayer (UUID playerId) {
        return turnPlayer.equals(playerId);
    }

    public boolean isPlayer (UUID targetId) {
        return players.values().stream().anyMatch(p -> p.id.equals(targetId));
    }

    public void forEachInZone (UUID playerId, Zone zone, Predicate<com.visitor.game.Card> filter, Consumer<UUID> cardIdConsumer) {
        getZone(playerId, zone).forEach(card -> {
            if(filter.test(card)){
                cardIdConsumer.accept(card.id);
            }
        });
    }

    public int countInZone (UUID playerId, Zone zone, Predicate<com.visitor.game.Card> cardConsumer) {
        Arraylist<Integer> count = new Arraylist<>();
        getZone(playerId, zone).forEach(card -> {
            if (cardConsumer.test(card)) {
                count.add(0);
            }
        });
        return count.size();
    }

    public void runIfInZone(UUID playerId, Zone zone, UUID cardId, Runnable function){
        if(isIn(playerId, zone, cardId)){
            function.run();
        }
    }

    public void runIfHasKnowledge (UUID playerId, CounterMap<Types.Knowledge> knowledge, Runnable effect) {
        if (getPlayer(playerId).hasKnowledge(knowledge)){
            effect.run();
        }
    }

    public void runIfInPlay (UUID cardId, Runnable r) {
        runIfInZone(UUID.randomUUID(), Both_Play, cardId, r);
    }

    public boolean isIn (UUID playerId, Zone zone, UUID cardID) {
        return getZone(playerId, zone).parallelStream().anyMatch(getCard(cardID)::equals);
    }

    public void putToBottomOfDeck (UUID cardId) {
        com.visitor.game.Card card = extractCard(cardId);
        getPlayer(card.controller).putToBottomOfDeck(card);
    }


    public void putToTopOfDeck (UUID cardId) {
        com.visitor.game.Card card = extractCard(cardId);
        getPlayer(card.controller).putToBottomOfDeck(card);
    }

    public void putToBottomOfDeck (UUID playerId, Arraylist<com.visitor.game.Card> toBottom) {
        com.visitor.game.Player player = getPlayer(playerId);
        toBottom.forEach(player::putToBottomOfDeck);
    }

    public boolean hasEnergy (UUID playerId, int i) {
        return getPlayer(playerId).energy >= i;
    }

    public boolean hasKnowledge (UUID playerId, CounterMap<Types.Knowledge> knowledge) {
        return getPlayer(playerId).hasKnowledge(knowledge);
    }

    public boolean canPlaySlow (UUID playerId) {
        return turnPlayer.equals(playerId)
                && activePlayer.equals(playerId)
                && stack.isEmpty()
                && (phase == MAIN_BEFORE || phase == MAIN_AFTER);
    }



    public boolean canStudy (UUID playerId) {
        return canPlaySlow(playerId)
                && getPlayer(playerId).numOfStudiesLeft > 0;
    }

    public void shuffleIntoDeck (UUID playerId, com.visitor.game.Card... cards) {
        getPlayer(playerId).shuffleIntoDeck(cards);
    }

    public boolean hasIn (UUID playerId, Zone zone, Predicate<com.visitor.game.Card> validTarget, int count) {
        return getZone(playerId, zone).parallelStream().filter(validTarget).count() >= count;
    }

    public void putTo (UUID playerId, com.visitor.game.Card c, Zone zone) {
        getZone(playerId, zone).add(c);
    }

    public void putTo (UUID playerId, com.visitor.game.Card c, Zone zone, int index) {
        getZone(playerId, zone).add(index, c);
    }

    public void putTo (UUID playerId, Arraylist<com.visitor.game.Card> cards, Zone zone) {
        getZone(playerId, zone).addAll(cards);
    }

    public Arraylist<com.visitor.game.Card> getTopCardsFromDeck (UUID playerId, int i) {
        return getPlayer(playerId).getFromTopOfDeck(i);
    }
}
