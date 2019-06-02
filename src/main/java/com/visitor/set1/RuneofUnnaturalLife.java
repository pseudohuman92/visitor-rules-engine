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
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class RuneofUnnaturalLife extends Item {
    
    public RuneofUnnaturalLife (String owner){
        super("Rune of Unnatural Life", 3, new Hashmap(BLACK, 1), 
                "Activate, Sacrifice ~:\n" +
"Draw a card from your scrapyard then\n" +
"discard a card.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game) && game.hasInstancesIn(controller, Card.class, VOID, 1);
    }

    @Override
    public void activate(Game game) {
        Arraylist<UUID> selected = game.selectFromZone(controller, SCRAPYARD, Predicates::any, 1, false);
        game.destroy(id);
        game.addToStack(new Ability(this,
            "Draw a card from your scrapyard then discard a card.",
            (x) -> {
                if (game.isIn(controller, selected.get(0), SCRAPYARD)){
                    game.drawByID(controller, selected.get(0));
                    game.discard(controller, 1);
                }
            }, selected));
    }
}
