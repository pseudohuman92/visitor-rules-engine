/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.sets.token.UnitToken;

import java.util.UUID;

import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.protocol.Types.Knowledge.YELLOW;

/**
 * @author pseudo
 */
public class Beesmirch extends Unit {

    public Beesmirch(Game game, UUID owner) {
        super(game, "Beesmirch",
                3, new CounterMap(YELLOW, 2),
                "{Y}{Y}{Y} - {2}: Play a 1/1 Spirit with flying.",
                3, 3,
                owner, Flying);

        activatable.addActivatedAbility(new ActivatedAbility(game, this, 2, "{Y}{Y}{Y} - {2}: Play a 1/1 Spirit with flying.",
                () -> UnitToken.Spirit_1_1(game, controller).resolve())
                .setKnowledgeRequirement(new CounterMap<>(YELLOW, 3)));
    }
}
