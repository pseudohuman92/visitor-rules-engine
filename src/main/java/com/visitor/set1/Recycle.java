/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Item;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Recycle extends Spell {

    UUID target;
    
    public Recycle(String owner) {
        super("Recycle", 3, new Hashmap(BLUE, 1), "Transform target item into Junk", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasIn(controller, BOTH_PLAY, Predicates::isItem, 1);
    }
    
    @Override
    protected void beforePlay(Game game) {
        targets = game.selectFromZone(controller, BOTH_PLAY, Predicates::isItem, 1, false);
        target = targets.get(0);
        
        
    }
    
    @Override
    protected void duringResolve (Game game){
        if(game.isIn(controller, target, BOTH_PLAY)){
            game.transformToJunk(this, target);
        }
    }    
}
