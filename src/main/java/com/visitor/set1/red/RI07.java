/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.red;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class RI07 extends Item {
    
    public RI07 (String owner){
        super("RI07", 1, new Hashmap(RED, 2), 
                "X, Purge X, Activate: \n" +
                "  Opponent purge X", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 1)&&game.hasCardsIn(controller, "scrapyard", 1);
    }
    
    @Override
    public void activate(Game game) {
        int x = game.selectX(controller, Math.min(game.getEnergy(controller), game.getZone(controller, "scrapyard").size()));
        Arraylist<UUID> selection = game.selectFromZone(controller, "scrapyard", c->{return true;}, x, false);
        UUID target = game.selectDamageTargets(controller, 1, false).get(0);
        game.spendEnergy(controller, x);
        Arraylist<Card> cards = game.extractAll(selection);
        game.putTo(controller, cards, "void");
        game.deplete(id);
        game.addToStack(new Activation(controller, "Deal " + x + " damage",
        (y) -> {
            game.dealDamage(target, x);
        }, new Arraylist(selection).putIn(target).putIn(id)));
    }
}
