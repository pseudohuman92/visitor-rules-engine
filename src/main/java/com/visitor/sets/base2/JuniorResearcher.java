package com.visitor.sets.base2;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class JuniorResearcher extends Unit {
    public JuniorResearcher(Game game, UUID owner) {
        super(game, "Junior Researcher", 1, new CounterMap<>(BLUE, 1),
                "When Junior Researcher enters play, you may study an additional time this turn.", 1, 2, owner);
        playable.addAfterResolveEffect(() -> game.addStudyCount(controller, 1));
    }
}
