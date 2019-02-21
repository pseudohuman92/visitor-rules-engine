/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.properties.Targeting;
import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI07 extends Item implements Targeting {
    
    public BI07 (String owner){
        super("BI07", 2, new Hashmap(BLACK, 2), 
                "3, Sacrifice ~, Activate: Possess target item.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted;
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.spendEnergy(controller, 3);
        ArrayList<UUID> selected = game.selectFromPlay(controller, this::validTarget, 1);
        game.destroy(id);
        game.addToStack(new Activation (controller,
            "Possess target item",
            (g, c) -> {
                if (g.isIn(c.controller, selected.get(0), "both play"))
                    g.possessTo(c.controller, selected.get(0), "play");
            }));
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item);
    }
}
