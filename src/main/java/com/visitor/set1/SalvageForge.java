/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.HAND;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;


/**
 *
 * @author pseudo
 */
public class SalvageForge extends Item {
    
    public SalvageForge (String owner){
        super("Salvage Forge", 1, new Hashmap(BLUE, 1), 
                "1, Discard 1: Transform ~ into AI02.", owner);
        subtypes.add("Kit");
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasIn(controller, HAND, Predicates::any, 1)
                && game.hasEnergy(controller, 1);
    }
    
    @Override
    public void activate(Game game) {
        game.discard(controller, 1);
        game.spendEnergy(controller, 1);
        game.addToStack(new Ability(this, "Transform ~ into AI02.",
            (x) -> { 
                if(game.isIn(controller, id, PLAY)) {
                    game.transformTo(this, this, new ScrapGrenade(this));
                }
        }));
    }
}
