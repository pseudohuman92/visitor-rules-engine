package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

import java.util.UUID;

public class Putters extends Extractors {
    public void putToBottomOfDeck(UUID cardId) {
        Card card = extractCard(cardId);
        getPlayer(card.controller).putToBottomOfDeck(card);
    }

    public void putToTopOfDeck(UUID cardId) {
        Card card = extractCard(cardId);
        getPlayer(card.controller).putToBottomOfDeck(card);
    }

    public void putToBottomOfDeck(UUID playerId, Arraylist<Card> toBottom) {
        com.visitor.game.Player player = getPlayer(playerId);
        toBottom.forEach(player::putToBottomOfDeck);
    }

    public void putTo(UUID playerId, Card c, Zone zone) {
        c.zone = zone;
        ((Arraylist<Card>)getZone(playerId, zone)).add(c);
    }

    public void putTo(UUID playerId, Card c, Zone zone, int index) {
        c.zone = zone;
        ((Arraylist<Card>)getZone(playerId, zone)).add(index, c);
    }

    public void putTo(UUID playerId, Arraylist<Card> cards, Zone zone) {
        cards.forEach(c -> c.zone = zone);
        ((Arraylist<Card>)getZone(playerId, zone)).addAll(cards);
    }

    public void addKnowledge(UUID playerId, CounterMap<Types.Knowledge> studyKnowledgeType) {
        getPlayer(playerId).addKnowledge(studyKnowledgeType);
    }
}
