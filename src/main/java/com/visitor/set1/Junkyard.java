/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Asset;
import com.visitor.card.types.Junk;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.BLUE;

/**
 *
 * @author Confuzzleinator
 */
public class Junkyard extends Asset {
    
    public Junkyard(String owner) {
        super("Junkyard", 3, new Hashmap(BLUE, 3),
                "Activate:\n"
                        + "Add a Junk to your hand.", 2, owner);
    }
    
    @Override
    public void activate(Game game) {
        deplete();
        game.addToStack(new Ability(this, 
            "Add a Junk to your hand.",
            (x) -> {
                game.putTo(controller, new Junk(controller), Game.Zone.HAND);
            }));
    }
}
