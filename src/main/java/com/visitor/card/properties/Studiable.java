package com.visitor.card.properties;

import com.visitor.card.Card;
import com.visitor.game.Player;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.function.Supplier;

public class Studiable {

    private final Card card;
    private final Game game;

    private final boolean purging;

    private final Arraylist<Supplier<Boolean>> studyConditions;
    private final Arraylist<TriConsumer<Player, Boolean, CounterMap<Types.Knowledge>>> studyEffects;


    public Studiable(Game game, Card card, TriConsumer<Player, Boolean, CounterMap<Types.Knowledge>> additionalStudy, boolean purging) {
        this.card = card;
        this.game = game;
        this.purging = purging;
        studyEffects = new Arraylist<>(additionalStudy);
        studyConditions = new Arraylist<>(() -> this.game.canStudy(this.card.controller) && this.card.zone == Base.Zone.Hand);

        studyEffects.add((player, regular, knowledge) -> {
            this.card.zone = null;
            if (!this.purging)
                player.deck.shuffleInto(this.card);
            player.addEnergy(1, false);
            player.addMaxEnergy(1);
            player.addKnowledge(knowledge);
            if (regular) {
                player.removeStudy(1);
            }
        });
    }

    public Studiable(Game game, Card card, TriConsumer<Player, Boolean, CounterMap<Types.Knowledge>> additionalStudy) {
        this(game, card, additionalStudy, false);
    }

    public Studiable(Game game, Card card) {
        this(game, card, (a, b, c) -> {});
    }

    public final boolean canStudy() {
        boolean b = true;
        for (Supplier<Boolean> s : studyConditions)
            b = b && s.get();
        return b;
    }

    /**
     * Called by the server when you choose to studyCard this card.
     * It increases player's maximum energy and adds knowledgePool.
     */
    public final void study(Player player, boolean regular, CounterMap<Types.Knowledge> knowledge) {
        for (TriConsumer<Player, Boolean, CounterMap<Types.Knowledge>>  s : studyEffects)
            s.accept(player, regular, knowledge);
    }


    public Studiable addStudyEffect(TriConsumer<Player, Boolean, CounterMap<Types.Knowledge>> additionalStudyEffect) {
        studyEffects.add(additionalStudyEffect);
        return this;
    }
}
