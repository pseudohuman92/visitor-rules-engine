package com.visitor.sets.base;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.visitor.card.types.Tome;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;

/**
 * @author pseudo
 */
public class MysticalMixology extends Tome {

    public MysticalMixology(Game game, UUID owner) {
        super(game, "Mystical Mixology", "Study: Gain {P}{P}", owner, new CounterMap<>(PURPLE, 2));
    }

}
