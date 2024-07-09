package com.visitor.sets.base2;

import com.visitor.card.types.Ritual;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class ASoulforASoul extends Ritual {
    public ASoulforASoul(Game game, UUID owner) {
        super(game, "A Soul for A Soul", 1,
                new CounterMap<>(PURPLE, 1),
                "Additional Cost - Destroy target ally unit.\nDestroy target enemy unit.",
                owner);

        playable.addTargetSingleUnit(Base.Zone.Play, Predicates.isAllyUnit(controller),
                game::destroy, "Select an ally unit to destroy.", true);

        playable.addTargetSingleUnit(Base.Zone.Both_Play, Predicates.isEnemyUnit(controller),
                game::destroy, "Select an enemy unit to destroy.", false);
    }
}
