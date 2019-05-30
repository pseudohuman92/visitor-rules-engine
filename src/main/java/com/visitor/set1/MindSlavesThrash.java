/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class MindSlavesThrash extends Item {
    
    public MindSlavesThrash (String owner){
        super("Mind Slave's Thrash", 1, new Hashmap(BLACK, 1), 
                "Condition - Control a card you don't own in play. Activate: Deal 2 damage", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.controlsUnownedCard(controller, "play");
    }

    @Override
    public void activate(Game game) {
        UUID target = game.selectDamageTargets(controller, 1, false).get(0);
        game.deplete(id);
        game.addToStack(new Activation(this,
            "deal 2 damage.",
            (x) -> {
                game.dealDamage(id, target, 2);
            }));
    }
}
