package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class UR04 extends Ritual {
    public UR04(Game game, String owner) {
        super(game, "UR01", 6,
                new CounterMap<>(BLUE, 3),
                "Return all cards to their controller's hands.",
                owner);

        playable
        .setResolveEffect(() ->
                game.returnAllCardsToHand()
        );
    }
}
