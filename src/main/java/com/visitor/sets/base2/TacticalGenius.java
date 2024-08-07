/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base2;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Blitz;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class TacticalGenius extends Unit {
    public TacticalGenius(Game game, UUID owner) {
        super(game, "Tactical Genius",
                2, new CounterMap(RED, 1),
                "When {~} enters play, each ally unit gains +1/+0 and Blitz until end of turn.",
                1, 1,
                owner);
        playable.addAfterResolveEffect(
            () -> game.getAllFrom(controller, Base.Zone.Play, Predicates.isAllyUnit(controller)).forEach(c -> {
                game.addAttackAndHealth(c.getId(),1, 0, true);
                game.addCombatAbility(c.getId(), Blitz, true);
        }));
    }
}
