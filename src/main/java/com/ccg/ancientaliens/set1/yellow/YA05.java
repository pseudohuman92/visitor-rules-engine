/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA05 extends Action {
    
    UUID selected;

    public YA05(String owner) {
        super("YA05", 1, new Hashmap(YELLOW, 1), 
                "Return target action from scrapyard to your hand.\n" +
                "Purge ~", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) 
                && game.hasAnInstanceIn(controller, Action.class, "scrapyard");
    }
    
    @Override
    public void play (Game game){
        selected = game.selectFromScrapyard(controller, c->{return c instanceof Action;}, 1).get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    } 
    
    @Override
    public void resolve (Game game){
        if(game.isIn(controller, selected, "scrapyard")){
            game.putTo(controller, game.extractCard(selected), "hand");
            game.putTo(controller, this, "void");
        }
    }    
}
