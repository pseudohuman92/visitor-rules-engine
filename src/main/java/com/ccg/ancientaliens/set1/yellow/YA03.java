/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA03 extends Action {

    UUID target; 
    
    public YA03(String owner) {
        super("YA03", 3, new Hashmap(YELLOW, 2), 
                "Cancel target card.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) && game.hasValidTargetsIn(controller, c->{return !(c instanceof Activation);}, 1, "stack");
    }
    
    @Override
    public void play (Game game){
        target = game.selectFromStack(controller, c->{return !(c instanceof Activation);}, 1).get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    } 
    
    @Override
    public void resolve (Game game){
        Card c = game.extractCard(target);
        game.putTo(c.controller, c, "scrapyard");
        game.putTo(controller, this, "scrapyard");
    }    
}
