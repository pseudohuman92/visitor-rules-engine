/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA02 extends Spell {

    UUID target; 
    
    public YA02(String owner) {
        super("YA02", 2, new Hashmap(YELLOW, 1), 
                "Cancel target spell.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) && game.hasInstancesIn(controller, Spell.class, "stack", 1);
    }
    
    @Override
    public void play (Game game){
        targets = game.selectFromZone(controller, "stack", c->{return c instanceof Spell;}, 1, false);
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
