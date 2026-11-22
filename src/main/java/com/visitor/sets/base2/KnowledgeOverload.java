package com.visitor.sets.base2;

import com.visitor.card.types.Ritual;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class KnowledgeOverload extends Ritual {
    public KnowledgeOverload(Game game, UUID owner) {
        super(game, "Knowledge Overload", 4, new CounterMap<>(BLUE, 3),
                "Deal damage to target enemy unit equal to your total knowledge. "
                        + "{B}{B}{B}{B} — You may target a player instead.", owner);
        playable.addTargetSingleCardOrPlayer(Base.Zone.Both_Play_With_Players,
                target -> Predicates.isEnemyUnit(controller).test(target)
                        || (game.getKnowledgeCount(controller, BLUE) >= 4 && Predicates.isPlayer(target)),
                "Choose an enemy unit, or a player with {B}{B}{B}{B}.",
                target -> game.dealDamage(getId(), target, game.getTotalKnowledge(controller)), true, false);
    }
}
