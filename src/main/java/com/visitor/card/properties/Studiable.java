package com.visitor.card.properties;

import com.visitor.card.types.Card;
import com.visitor.game.Game;
import com.visitor.game.Player;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Studiable {

    private Card card;
    private Game game;

    private Supplier<Boolean> canStudy;
    private Consumer<Boolean> study;
    private Supplier<Hashmap<Types.Knowledge, Integer>> getKnowledgeType;

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

    public final Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
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
            Hashmap<Types.Knowledge, Integer> knowledgeType = new Hashmap<>();
            card.knowledge.forEach((k, i) -> knowledgeType.putIn(k, 1));
            return knowledgeType;
        };
    }


    //Setters
    public Studiable setGetKnowledgeType(Supplier<Hashmap<Types.Knowledge, Integer>> getKnowledgeType) {
        this.getKnowledgeType = getKnowledgeType;
        return this;
    }
}
