package com.visitor.sets.base2;

import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

public class Prick extends Cantrip {
    public Prick(Game game, UUID owner) {
        super(game, "Prick", 0,
                new CounterMap<>(),
                "Deal 1 damage to any enemy",
                owner);

        playable.addTargetSingleUnitOrPlayer(Base.Zone.Play, Predicates.isEnemy(controller), t -> game.dealDamage(getId(), t, 1), false);
    }
}
