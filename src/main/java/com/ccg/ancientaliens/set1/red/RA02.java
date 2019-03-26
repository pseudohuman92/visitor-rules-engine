/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.red;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Hashmap;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class RA02 extends Spell {

    UUID target; 
    
    public RA02(String owner) {
        super("RA02", 2, new Hashmap(RED, 2), 
                "Put target item on top of controller's deck.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasInstancesIn(controller, Item.class, "both play", 1);
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, "both play", c->{return c instanceof Item;}, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        if(game.isIn(controller, target, "both play")){
            Card c = game.extractCard(target);    
            game.putTo(c.controller, c, "deck", 0);
        }
        game.putTo(controller, this, "scrapyard");
    }    
}
