/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1.red;

import com.visitor.card.types.Spell;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.RED;
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
        game.damagePlayer(game.getOpponentName(controller), 4);
    }    
}
