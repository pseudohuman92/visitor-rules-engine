package com.visitor.sets.base2;

import com.visitor.card.types.Ritual;
import com.visitor.card.Card;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class Contagion extends Ritual {
    public Contagion(Game game, UUID owner) {
        super(game, "Contagion", 4,
                new CounterMap<>(PURPLE, 2),
                "Destroy all units. Lose 1 health for each enemy destroyed unit.",
                owner);
        playable.addResolveEffect(() -> game.getAllFrom(Base.Zone.Both_Play, Predicates::isUnit)
            .forEach(c -> {
                game.destroy(c.getId());
                if (Predicates.isEnemy(controller).test(c)) {
                    game.loseHealth(controller, 1);
                }
            }));
    }
}
