package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.types.Ritual;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class MassExecution extends Ritual {
    private int destroyedCount;

    public MassExecution(Game game, UUID owner) {
        super(game, "Mass Execution", 4, new CounterMap<>(RED, 2).add(PURPLE, 2),
                "Destroy any number of ally units. Deal 2 damage to your opponent for each unit destroyed this way.", owner);
        playable.addResolveEffect(() -> destroyedCount = 0);
        playable.addTargetMultipleCards(Base.Zone.Play,
                Predicates.isAllyUnit(controller).and(Predicates::isUnit), 0, 999,
                "Choose any number of ally units to destroy.", id -> {
                    Card unit = game.getCard(id);
                    if (unit != null && unit.zone == Base.Zone.Play && Predicates.isUnit(unit)) {
                        game.destroy(id);
                        destroyedCount++;
                    }
                }, false);
        playable.addResolveEffect(() -> game.dealDamage(getId(), game.getOpponentId(controller), 2 * destroyedCount));
    }
}
