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
import static com.visitor.game.Game.Zone.HAND;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class ScrapGrenade extends Asset {
    
    public ScrapGrenade (SalvageForge c){
        super("Scrap Grenade", 3, new Hashmap(BLUE, 3), 
                "Purge a Junk from your hand: Deal 3 damage", c.controller);
        copyPropertiesFrom(c);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasIn(controller, HAND, Predicates::isJunk, 1);
    }
    
    @Override
    public void activate(Game game) {
        Arraylist<UUID> selected = game.selectFromZone(controller, HAND, Predicates::isJunk, 1, false);
        game.purge(controller, selected.get(0));
        UUID target = game.selectDamageTargets(controller, 1, false).get(0);
        game.addToStack(new Ability(this, "Deal 3 damage",
        (y) -> {
            game.dealDamage(id, target, 3);
        }, new Arraylist<>(target)));   }
}
