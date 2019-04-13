/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.red;

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
public class RA05 extends Spell {

    UUID target; 
    
    public RA05(String owner) {
        super("RA05", 1, new Hashmap(RED, 2), 
            "Additional Cost \n" +
            "  Return an item you control to your hand. \n" +
            "Opponent purges 4.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) && game.hasInstancesIn(controller, Item.class, "play", 1);
    }
    
    @Override
    public void play (Game game){
        target = game.selectFromZone(controller, "play", c->{return c instanceof Item;}, 1, false).get(0);
        game.putTo(controller, game.extractCard(target), "hand");
        super.play(game);
    }  
    
    @Override
    public void resolveEffect (Game game){
        game.purge(game.getOpponentName(controller), 4);
    }    
}
