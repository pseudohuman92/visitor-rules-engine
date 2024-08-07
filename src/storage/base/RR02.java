package com.visitor.sets.base;

import com.visitor.card.types.Ritual;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.card.containers.Damage;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.RED;

public class RR02 extends Ritual {

    public RR02(Game game, UUID owner) {
        super(game, "RR02", 4,
                new CounterMap<>(RED, 2),
                "Deal 5 damage to target unit.",
                owner);

        playable
                .setTargetSingleUnit(null, null, cardId -> game.dealDamage(this.id, cardId, new Damage(5)), null);
    }
}
