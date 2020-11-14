package com.visitor.sets.base;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.visitor.card.types.Tome;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Hashmap;

import static com.visitor.protocol.Types.Knowledge.*;

/**
 * @author pseudo
 */
public class DarkPhonebook extends Tome {

    public DarkPhonebook(Game game, String owner) {
        super(game, "Dark Phonebook", "Study: Gain {G}{P}", owner, () -> new CounterMap<>(PURPLE, 1).add(GREEN, 1));
    }

}
