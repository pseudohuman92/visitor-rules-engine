package com.visitor.sets.base2;

import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Blitz;
import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class LastStand extends Cantrip {
    public LastStand(Game game, UUID owner) {
        super(game, "Last Stand", 3, new CounterMap<>(RED, 2).add(PURPLE, 1),
                "Each ally unit gains +2/+0 and Blitz until end of turn. "
                        + "At the end of your turn, destroy each ally unit that attacked this turn.", owner);
        playable.addResolveEffect(() -> {
            game.getAllFrom(controller, Base.Zone.Play, Predicates.isAllyUnit(controller)).forEach(unit -> {
                game.addAttackAndHealth(unit.getId(), 2, 0, true);
                game.addCombatAbility(unit.getId(), Blitz, true);
            });
            game.armLastStand(controller);
        });
    }
}
