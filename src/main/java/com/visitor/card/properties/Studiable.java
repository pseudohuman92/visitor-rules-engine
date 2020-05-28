package com.visitor.card.properties;

import com.visitor.card.Card;
import com.visitor.game.Game;
import com.visitor.game.Player;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Studiable {

    private Card card;
    private Game game;

    private Supplier<Boolean> canStudy;
    private Consumer<Boolean> study;
    private Supplier<CounterMap<Types.Knowledge>> getKnowledgeType;

    public Studiable(Game game, Card card) {
        this.card = card;
        this.game = game;

        setDefaultCanStudy();
        setDefaultGetKnowledgeType();
        setDefaultStudy();
    }

    public final boolean canStudy() {
        return canStudy.get();
    }

    /**
     * Called by the server when you choose to studyCard this card.
     * It increases player's maximum energy and adds knowledgePool.
     */
    public final void study(boolean regular) {
        study.accept(regular);
    }

    public final CounterMap<Types.Knowledge> getKnowledgeType() {
        return getKnowledgeType.get();
    }

    //Default Setters
    public void setDefaultCanStudy() {
        canStudy = () -> game.canStudy(card.controller);
    }

    public void setDefaultStudy() {
        study = (regular) -> {
            Player player = game.getPlayer(card.controller);
            player.voidPile.add(card);
            player.energy++;
            player.maxEnergy++;
            player.addKnowledge(getKnowledgeType());
            if (regular) {
                player.numOfStudiesLeft--;
            }
        };
    }

    public void setDefaultGetKnowledgeType() {
        getKnowledgeType = () -> {
            CounterMap<Types.Knowledge> knowledgeType = new CounterMap<>();
            card.knowledge.forEach((k, i) -> knowledgeType.add(k));
            return knowledgeType;
        };
    }


    //Setters
    public Studiable setGetKnowledgeType(Supplier<CounterMap<Types.Knowledge>> getKnowledgeType) {
        this.getKnowledgeType = getKnowledgeType;
        return this;
    }
}
