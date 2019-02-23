/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.UUIDHelper;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class YI03 extends Item {
    
    public YI03 (String owner){
        super("YI03", 3, new Hashmap(YELLOW, 3), 
                "\"2, Activate: \n" +
                "  Create and draw a YI01 or YI02.\"", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 2);
    }
    
    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 2);
        game.addToStack(new Activation(controller, "Create and draw YI01 or YI02",
        (g, c) -> {
            ArrayList<Card> choices = new ArrayList<>();
            choices.add(new Activation(controller, "Create and draw YI01",
                (g1, c1) -> {
                    g1.putTo(controller, new YI01(controller), "hand");
            }));
            choices.add(new Activation(controller, "Create and draw YI02",
                (g2, c2) -> {
                    g2.putTo(controller, new YI02(controller), "hand");
            }));
            ArrayList<UUID> selection = g.selectFromList(controller, choices, cx->{return true;}, 1, false);
            UUIDHelper.getInList(choices, selection).get(0).resolve(g);
        }));
    }
}
