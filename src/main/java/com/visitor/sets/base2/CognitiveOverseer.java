package com.visitor.sets.base2;

import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Unit;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class CognitiveOverseer extends Unit {
    public CognitiveOverseer(Game game, UUID owner) {
        super(game, "Cognitive Overseer", 5, new CounterMap<>(BLUE, 4),
                "Whenever you study, draw a card. The first time you study each turn, you may study an additional time.",
                3, 5, owner);
        triggering.addEventChecker(EventChecker.of(game, this, Event.EventType.Study,
                e -> controller.equals(e.data.get(0)), e -> {
                    game.draw(controller, 1);
                    if (game.getStudiesThisTurn(controller) == 1) game.addStudyCount(controller, 1);
                }));
    }
}
