/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class YI06 extends Item {
    
    public YI06 (String owner){
        super("YI06", 2, new Hashmap(YELLOW, 2), 
                "\"Sacrifice an item, Activate:\n" +
                "  Opponent purges X, \n" +
                "  where X is # of charge counter on sacrificed item.\"", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game) && game.hasInstancesIn(controller, Item.class, PLAY, 1);
    }
    
    @Override
    public void activate(Game game) {
        UUID selection = game.selectFromZone(controller, PLAY, Predicates::isItem, 1, false).get(0);
        int x = game.getCard(selection).counters.getOrDefault(CHARGE, 0);
        game.destroy(selection);
        game.deplete(id);
        game.addToStack(new Ability(this, game.getOpponentName(controller)+" purges " + x,
        (y) -> {
            game.damagePlayer(id, game.getOpponentName(controller), x);
        }));
    }
}
