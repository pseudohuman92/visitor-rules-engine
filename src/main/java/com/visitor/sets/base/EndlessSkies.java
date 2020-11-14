package com.visitor.sets.base;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.visitor.card.types.Tome;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;

import static com.visitor.protocol.Types.Knowledge.*;

/**
 * @author pseudo
 */
public class EndlessSkies extends Tome {

    public EndlessSkies (Game game, String owner) {
        super(game, "Endless Skies", "Study: Gain {U}{Y}", owner, new CounterMap<>(BLUE, 1).add(YELLOW, 1));
    }

}
