package com.visitor.card.properties;

import com.visitor.game.Card;
import com.visitor.game.Player;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Studiable {

    private final Card card;
    private final Game game;

    private Supplier<Boolean> studyCondition;
    private BiConsumer<Player, Boolean> study;
    private Supplier<CounterMap<Types.Knowledge>> getKnowledgeTypes;

    public Studiable(Game game, Card card) {
        this.card = card;
        this.game = game;

        setDefaultStudyCondition();
        setDefaultGetKnowledgeTypes();
        setDefaultStudy();
    }

    public final boolean canStudy() {
        return studyCondition.get();
    }

    /**
     * Called by the server when you choose to studyCard this card.
     * It increases player's maximum energy and adds knowledgePool.
     */
    public final void study(Player player, boolean regular) {
        study.accept(player, regular);
    }

    public final CounterMap<Types.Knowledge> getKnowledgeTypes() {
        return getKnowledgeTypes.get();
    }

    //Default Setters
    public void setDefaultStudyCondition() {
        studyCondition = () -> game.canStudy(card.controller);
    }

    public void setDefaultStudy() {
        study = (player, regular) -> {
            card.zone = Game.Zone.Void;
            player.voidPile.add(card);
            player.energy++;
            player.maxEnergy++;
            player.addKnowledge(getKnowledgeTypes());
            if (regular) {
                player.numOfStudiesLeft--;
            }
        };
    }

    public void setDefaultGetKnowledgeTypes() {
        getKnowledgeTypes = () -> {
            if (card.knowledge.keySet().size() < 2) {
                CounterMap<Types.Knowledge> knowledgeType = new CounterMap<>();
                card.knowledge.forEach((k, i) -> knowledgeType.add(k));
                return knowledgeType;
            } else {
                return game.selectKnowledge(card.controller, card.knowledge.keySet());
            }
        };
    }

    //Setters
    public Studiable setGetKnowledgeTypes(Supplier<CounterMap<Types.Knowledge>> getKnowledgeTypes) {
        this.getKnowledgeTypes = getKnowledgeTypes;
        return this;
    }
}
