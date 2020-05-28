package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.*;
import static com.visitor.protocol.Types.Knowledge.GREEN;

public class GR02 extends Ritual {

    public GR02(Game game, String owner) {
        super(game, "UR01", 3,
                new CounterMap<>(GREEN, 1),
            "Return target card from your discard pile to your hand.",
                owner);

        playable
        .setCanPlayAdditional(() ->
            game.hasIn(playable.card.controller, Discard_Pile, Predicates::any, 1)
        )
        .setBeforePlay(() ->
            targets.add(game.selectFromZone(playable.card.controller, Discard_Pile, Predicates::any, 1, false).get(0))
        )
        .setResolveEffect(() ->
            game.getCard(targets.get(0)).returnToHand()
        );
    }
}
