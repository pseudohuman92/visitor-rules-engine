package com.visitor.game.parts;

import com.visitor.game.Card;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.protocol.Types;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.visitor.protocol.Types.SelectFromType.NOTYPE;

public class Getters extends Base {
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

    public UUID getPlayerId(String username) {
        for (Player p : players.values()) {
            if (p.username.equals(username)) return p.id;
        }
        return null;
    }

    public UUID getOpponentId(UUID playerId) {
        for (UUID id : players.keySet()) {
            if (!id.equals(playerId)) {
                return id;
            }
        }
        return null;
    }

    public Card getCard(UUID targetID) {
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

    private Card getCardFromZone(UUID playerId, Game.Zone zone, UUID cardId) {
        Arraylist<Card> card = new Arraylist<>();
        getZone(playerId, zone).forEach(c -> {
            if (c.id.equals(cardId))
                card.add(c);
        });
        assert (card.size() < 2);
        return card.getOrDefault(0, null);
    }

    public Arraylist<Card> getAllFrom(UUID playerId, Game.Zone zone, Predicate<Card> pred) {
        Arraylist<Card> cards = new Arraylist<>();
        getZone(playerId, zone).forEach(c -> {
            if (pred.test(c)) {
                cards.add(c);
            }
        });
        return cards;
    }

    public Arraylist<Card> getAllFrom(Game.Zone zone, Predicate<Card> pred) {
        return getAllFrom(turnPlayer, zone, pred).putAllIn(getAllFrom(getOpponentId(turnPlayer), zone, pred));
    }

    Arraylist<Card> getAllZones(UUID playerId) {
            Arraylist<Card> cards = new Arraylist<>();
            return cards.putAllIn(
                getPlayer(playerId).deck).putAllIn(
                getPlayer(playerId).hand).putAllIn(
                getPlayer(playerId).playArea).putAllIn(
                getPlayer(playerId).discardPile).putAllIn(
                getPlayer(playerId).voidPile);
    }

    public Arraylist<Card> getAll(Predicate<Card> pred) {
        Arraylist<Card> cards = new Arraylist<>();
        getAllZones(turnPlayer).forEach(c -> {
            if (pred.test(c)) {
                cards.add(c);
            }
        });
        getAllZones(getOpponentId(turnPlayer)).forEach(c -> {
            if (pred.test(c)) {
                cards.add(c);
            }
        });
        return cards;
    }

    public Arraylist<UUID> getAllIds(Predicate<Card> pred) {
        return new Arraylist<>(getAll(pred).transform(c -> c.id));
    }

    public Arraylist<UUID> getAllIdsPlayers(Predicate<Player> pred) {
        Arraylist<UUID> players = new Arraylist<>();
        if (pred.test(getPlayer(turnPlayer))) {
            players.add(getPlayer(turnPlayer).id);
        }
        if (pred.test(getPlayer(getOpponentId(turnPlayer)))) {
            players.add(getPlayer(getOpponentId(turnPlayer)).id);
        }
        return players;
    }

    Arraylist<Card> getAll(List<UUID> list) {
        return new Arraylist<>(list.stream().map(this::getCard).collect(Collectors.toList()));
    }

    public UUID getId() {
        return id;
    }

    public Arraylist<com.visitor.game.Card> getTopCardsFromDeck(UUID playerId, int i) {
        return getPlayer(playerId).getFromTopOfDeck(i);
    }
}
