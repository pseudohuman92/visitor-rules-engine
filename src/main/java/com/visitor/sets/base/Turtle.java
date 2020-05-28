/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.card.properties.Combat.CombatAbility.Defender;
import static com.visitor.protocol.Types.Knowledge.BLUE;

/**
 * @author pseudo
 */
public class Turtle extends Unit {

    public Turtle(Game game, String owner) {
        super(game, "Turtle",
                1, new CounterMap(BLUE, 1),
                "",
                0, 5,
                owner, Defender);
    }
}
