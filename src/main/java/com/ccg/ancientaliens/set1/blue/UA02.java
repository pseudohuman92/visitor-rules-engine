/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class UA02 extends Spell {

    UUID target;
    
    public UA02(String owner) {
        super("UA02", 3, new Hashmap(BLUE, 1), "Transform target item into Junk", owner);
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
    public void resolveEffect (Game game){
        if(game.isIn(controller, target, "both play")){
            game.transformToJunk(target);
        }
    }    
}
