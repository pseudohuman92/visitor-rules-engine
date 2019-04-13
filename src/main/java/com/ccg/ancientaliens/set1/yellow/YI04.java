/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Arraylist;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import com.ccg.ancientaliens.helpers.Hashmap;
import static com.ccg.ancientaliens.protocol.Types.Counter.CHARGE;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class YI04 extends Item {
    
    public YI04 (String owner){
        super("YI04", 2, new Hashmap(YELLOW, 2), 
                "2X, Activate: \n" +
                "  Target item Charge X.", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted;
    }
    
    @Override
    public void activate(Game game) {
        int x = game.selectX(controller, game.getPlayer(controller).energy/2);
        UUID selection = game.selectFromZone(controller, "both play", c->{return c instanceof Item;}, 1, false).get(0);
        game.spendEnergy(controller, 2 * x);
        game.deplete(id);
        game.addToStack(new Activation(controller, "Charge " + x,
        (y) -> {
            if(game.isIn(controller, selection, "both play")){
                game.getCard(selection).addCounters(CHARGE, x);
            }
        }, new Arraylist<>(selection)));
    }
}
