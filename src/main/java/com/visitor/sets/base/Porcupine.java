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
public class Porcupine extends Unit {

    public Porcupine(Game game, String owner) {
        super(game, "Porcupine",
                2, new CounterMap(BLUE, 1),
                "",
                1, 5,
                owner, Defender);
    }
}
