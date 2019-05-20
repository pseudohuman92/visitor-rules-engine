/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA05 extends Spell {
    
    /**
     *
     * @param owner
     */
    public BA05(String owner) {
        super("BA05", 1, new Hashmap(BLACK, 1), 
        "Opponent discards an spell.", owner);
    }
    
    @Override
    public void resolveEffect (Game game){
        Arraylist<UUID> selected = game.selectFromZone(game.getOpponentName(controller), "hand", c->{return c instanceof Spell;}, 1, 
                    !game.hasInstancesIn(game.getOpponentName(controller), Spell.class, "hand", 1));
        if(!selected.isEmpty()){
            game.discard(game.getOpponentName(controller), selected.get(0));
        }
    }
    
}
