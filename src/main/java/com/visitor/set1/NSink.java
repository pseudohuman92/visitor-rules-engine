/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class NSink extends Item {
    
    public NSink (String owner){
        super("N-Sink", 2, new Hashmap(YELLOW, 2), 
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
        UUID selection = game.selectFromZone(controller, BOTH_PLAY, Predicates::isItem, 1, false).get(0);
        game.spendEnergy(controller, 2 * x);
        game.deplete(id);
        game.addToStack(new Ability(this, "Charge " + x,
        (y) -> {
            if(game.isIn(controller, selection, BOTH_PLAY)){
                game.getCard(selection).addCounters(CHARGE, x);
            }
        }, new Arraylist<>(selection)));
    }
}
