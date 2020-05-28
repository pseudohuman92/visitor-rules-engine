package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class UR01 extends Ritual {
    public UR01(Game game, String owner) {
        super(game, "UR01", 6,
                new CounterMap<>(BLUE, 2),
                "Return up to 3 target units to their controller's hands.",
                owner);

        playable
        .setCanPlayAdditional(() ->
            game.hasIn(playable.card.controller, Both_Play, Predicates::isUnit, 1)
        )
        .setBeforePlay(() ->
            targets = game.selectFromZone(playable.card.controller, Both_Play, Predicates::isUnit,3,true)
       )
        .setResolveEffect(() ->
            targets.forEach(cardId -> game.getCard(cardId).returnToHand())
        );
    }
}
