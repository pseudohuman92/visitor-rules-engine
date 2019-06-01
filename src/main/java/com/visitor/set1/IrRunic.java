/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.HAND;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class IrRunic extends Spell {
    
    /**
     *
     * @param owner
     */
    public IrRunic(String owner) {
        super("Ir-Runic", 1, new Hashmap(BLACK, 1), 
        "Opponent discards an spell.", owner);
    }
    
    @Override
    public void resolveEffect (Game game){
        Arraylist<UUID> selected = game.selectFromZone(game.getOpponentName(controller), HAND, Predicates::isSpell, 1, 
                    !game.hasInstancesIn(game.getOpponentName(controller), Spell.class, HAND, 1));
        if(!selected.isEmpty()){
            game.discard(game.getOpponentName(controller), selected.get(0));
        }
    }
    
}
