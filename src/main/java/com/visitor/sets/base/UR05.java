package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class UR05 extends Ritual {
    public UR05(Game game, String owner) {
        super(game, "UR01", 3,
                new CounterMap<>(BLUE, 1),
                "Draw 3 cards.\nDiscard 1 card.",
                owner);

        playable
        .setResolveEffect(() -> {
            game.draw(playable.card.controller, 3);
            game.discard(playable.card.controller, 1);
        });
    }
}
