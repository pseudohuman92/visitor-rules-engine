package com.visitor.sets.base2;

import com.visitor.card.properties.Damagable;
import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.RED;

public class PuncturingRounds extends Cantrip {
    public PuncturingRounds(Game game, UUID owner) {
        super(game, "Puncturing Rounds", 1,
                new CounterMap<>(RED, 1),
                "Target ally unit you control gains +3/+0 and Trample until end of turn.",
                owner);

        playable.addTargetSingleUnit(Base.Zone.Play, Predicates.isAlly(controller), t -> {
           game.addAttackAndHealth(t, 3, 0, true);
           game.addCombatAbility(t, Damagable.CombatAbility.Trample, true);
        }, "", false);
    }
}
