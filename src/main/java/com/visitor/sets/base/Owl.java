/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.*;
import static com.visitor.protocol.Types.Knowledge.BLUE;

/**
 * @author pseudo
 */
public class Owl extends Unit {

    public Owl(Game game, String owner) {
        super(game, "Owl",
                3, new CounterMap(BLUE, 2),
                "",
                2, 2,
                owner, Flying, Lifelink);
    }
}
