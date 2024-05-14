package com.visitor.card.properties;

import com.google.protobuf.GeneratedMessage;
import com.visitor.game.Card;
import com.visitor.game.Player;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;

import java.util.UUID;
import java.util.function.Predicate;

public class Targeting {

    private final Game game;
    private int minTargets;
    private int maxTargets;
    private final Predicate<Card> cardPredicate;
    private final Predicate<Player> playerPredicate;
    private String targetingMessage;

    public Targeting(
            Game game,
            int minTargets,
            int maxTargets,
            Predicate<Card> cardPredicate,
            Predicate<Player> playerPredicate,
            String targetingMessage) {
        this.game = game;
        this.minTargets = minTargets;
        this.maxTargets = maxTargets;
        this.cardPredicate = cardPredicate != null ? cardPredicate : Predicates::none;
        this.playerPredicate = playerPredicate != null ? playerPredicate : Predicates::none;
        this.targetingMessage = targetingMessage != null ? targetingMessage : "";
    }

    public int getMinTargets() {
        return minTargets;
    }

    public int getMaxTargets() {
        return maxTargets;
    }

    public String getTargetingMessage() {
        return targetingMessage;
    }

    public Arraylist<UUID> getAllPossibleTargets(){return game.getAllIds(cardPredicate).putAllIn(game.getAllIdsPlayers(playerPredicate)); }


    public Types.Targeting.Builder toTargetingBuilder() {
        return Types.Targeting.newBuilder().
                setMinTargets(minTargets)
                .setMaxTargets(maxTargets)
                .setTargetMessage(targetingMessage)
                .addAllPossibleTargets(getAllPossibleTargets().transformToStringList());
    }

    public Predicate<Card> getCardPredicate() { return cardPredicate;
    }

    public Predicate<Player> getPlayerPredicate() { return playerPredicate;
    }
}
