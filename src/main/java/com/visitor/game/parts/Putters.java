package com.visitor.game.parts;

import com.visitor.game.Card;
import com.visitor.helpers.Arraylist;

import java.util.UUID;

public class Putters extends Extractors {
    public void putToBottomOfDeck(UUID cardId) {
        com.visitor.game.Card card = extractCard(cardId);
        getPlayer(card.controller).putToBottomOfDeck(card);
    }


    public void putToTopOfDeck(UUID cardId) {
        com.visitor.game.Card card = extractCard(cardId);
        getPlayer(card.controller).putToBottomOfDeck(card);
    }

    public void putToBottomOfDeck(UUID playerId, Arraylist<Card> toBottom) {
        com.visitor.game.Player player = getPlayer(playerId);
        toBottom.forEach(player::putToBottomOfDeck);
    }

    public void putTo(UUID playerId, com.visitor.game.Card c, Zone zone) {
        getZone(playerId, zone).add(c);
    }

    public void putTo(UUID playerId, com.visitor.game.Card c, Zone zone, int index) {
        getZone(playerId, zone).add(index, c);
    }

    public void putTo(UUID playerId, Arraylist<com.visitor.game.Card> cards, Zone zone) {
        getZone(playerId, zone).addAll(cards);
    }
}
