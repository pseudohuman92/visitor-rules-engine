package com.visitor.sets.base2;

import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.RED;

public class ExplosiveShot extends Cantrip {
    public ExplosiveShot(Game game, UUID owner) {
        super(game, "Explosive Shot", 1,
                new CounterMap<>(RED, 1),
                "Deal 2 damage to any enemy.\n{R}{R}{R} - Deal 3 damage instead.",
                owner);

        playable.addTargetSingleUnitOrPlayer(Base.Zone.Both_Play_With_Players,
                Predicates.isEnemy(controller),
                (t) -> {
                    if (game.hasKnowledge(controller, new CounterMap<>(RED, 3))){
                        game.dealDamage(getId(), t, 3);
                    } else {
                        game.dealDamage(getId(), t, 2);
                    }
                }, false);
    }
}
