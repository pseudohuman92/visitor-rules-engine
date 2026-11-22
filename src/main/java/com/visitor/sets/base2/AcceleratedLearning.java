package com.visitor.sets.base2;

import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class AcceleratedLearning extends Cantrip {
    public AcceleratedLearning(Game game, UUID owner) {
        super(game, "Accelerated Learning", 1, new CounterMap<>(BLUE, 1),
                "You may study an additional time this turn. Draw a card.", owner);
        playable.addResolveEffect(() -> {
            game.addStudyCount(controller, 1);
            game.draw(controller, 1);
        });
    }
}
