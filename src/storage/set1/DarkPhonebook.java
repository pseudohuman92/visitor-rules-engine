/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;

import com.visitor.card.types.Tome;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;

import static com.visitor.protocol.Types.Knowledge.BLACK;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public class DarkPhonebook extends Tome {

    public DarkPhonebook(Game game, String owner) {
        super(game, "Dark Phonebook", "Study: Gain BG", owner, () -> new Hashmap(BLACK, 1).putIn(GREEN, 1));
    }

}
