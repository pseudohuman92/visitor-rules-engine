/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;

import com.visitor.card.types.Tome;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;

import static com.visitor.protocol.Types.Knowledge.GREEN;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 * @author pseudo
 */
public class DestructiveLeadership extends Tome {

    public DestructiveLeadership(Game game, String owner) {
        super(game, "Destructive Leadership", "Study: Gain GR", owner, () -> new Hashmap(GREEN, 1).putIn(RED, 1));
    }

}
