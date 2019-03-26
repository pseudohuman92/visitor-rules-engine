/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA01 extends Spell {

    UUID target;
    
    public BA01(String owner) {
        super("BA01", 3, new Hashmap(BLACK, 2), "Possess target item that costs 3 or less.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasValidTargetsIn(controller, c->{return (c instanceof Item && c.cost <= 3);}, 1, "both play");
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, "both play", c->{return (c instanceof Item && c.cost <= 3);}, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        if(game.isIn(controller, target, "both play")){
            game.possessTo(controller, target, "play");
        }
        game.putTo(controller, this, "scrapyard");
    }
}
