package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.containers.ActivatedAbility;
import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Asset;
import com.visitor.game.Event;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class ResearchLaboratory extends Asset {
    public ResearchLaboratory(Game game, UUID owner) {
        super(game, "Research Laboratory", 2, new CounterMap<>(BLUE, 2),
                "The first time you study each turn, gain 1 energy this turn. "
                        + "{Use}: Put one research counter on a card in your hand.", owner);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Study,
                e -> controller.equals(e.data.get(0)) && game.getStudiesThisTurn(controller) == 1,
                e -> game.addEnergy(controller, 1, true)));
        activatable.addActivatedAbility(new ActivatedAbility(game, this, 0,
                "{Use}: Put one research counter on a card in your hand.")
                .addTargeting(Base.Zone.Hand, Predicates.controlledBy(controller), 1, 1,
                        "Choose a card in your hand.", id -> {
                            Card card = game.getCard(id);
                            if (card != null && card.zone == Base.Zone.Hand) card.addResearchProgress(1, false);
                        }, false)
                .setDepleting());
    }
}
