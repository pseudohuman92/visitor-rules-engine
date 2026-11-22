package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.types.Ritual;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Blitz;
import static com.visitor.card.properties.Damagable.CombatAbility.Decay;
import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class ViolentResurrection extends Ritual {
    public ViolentResurrection(Game game, UUID owner) {
        super(game, "Violent Resurrection", 2, new CounterMap<>(RED, 1).add(PURPLE, 2),
                "Purge target unit from your discard pile. Create a copy of that unit. It gains Blitz and Decay.", owner);
        playable.addTargetSingleCard(Base.Zone.Discard_Pile, Predicates::isUnit,
                "Purge a unit from your discard pile.", id -> {
                    Card source = game.getCard(id);
                    if (source == null || source.zone != Base.Zone.Discard_Pile) return;
                    Card copy = source.freshCopy(controller);
                    game.purge(id);
                    copy.addCombatAbility(Blitz, false);
                    copy.addCombatAbility(Decay, false);
                    copy.resolve();
                }, false);
    }
}
