package com.visitor.sets.base;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.visitor.card.types.Tome;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class TransformYourself extends Tome {

    public TransformYourself(Game game, String owner) {
        super(game, "Transform Yourself", "Study: Gain {G}{U}", owner, new CounterMap<>(BLUE, 1).add(GREEN, 1));
    }
}
