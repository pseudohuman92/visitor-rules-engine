/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import static com.visitor.game.Game.Zone.VOID;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.RED;
import static java.lang.Math.min;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class ShockTroop extends Item {
    
    public ShockTroop (String owner){
        super("Shock Troop", 1, new Hashmap(RED, 2), 
                "X, Purge X, Activate: \n" +
                "  Opponent purge X", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game) && game.hasEnergy(controller, 1)&&game.hasCardsIn(controller, SCRAPYARD, 1);
    }
    
    @Override
    public void activate(Game game) {
        int x = game.selectX(controller, min(game.getEnergy(controller), game.getZone(controller, SCRAPYARD).size()));
        Arraylist<UUID> selection = game.selectFromZone(controller, SCRAPYARD, Predicates::any, x, false);
        UUID target = game.selectDamageTargets(controller, 1, false).get(0);
        game.spendEnergy(controller, x);
        Arraylist<Card> cards = game.extractAll(selection);
        game.putTo(controller, cards, VOID);
        game.deplete(id);
        game.addToStack(new Ability(this, "Deal " + x + " damage",
        (y) -> {
            game.dealDamage(id, target, x);
        }, new Arraylist(selection).putIn(target)));
    }
}
