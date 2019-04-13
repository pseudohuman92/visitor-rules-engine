/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA03 extends Spell {

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
        targets = game.selectFromZone(controller, "stack", c->{return !(c instanceof Activation);}, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    } 
    
    @Override
    public void resolveEffect (Game game){
        Card c = game.extractCard(target);
        game.putTo(c.controller, c, "scrapyard");
    }    
}
