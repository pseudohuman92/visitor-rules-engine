/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.HAND;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import static com.visitor.game.Game.Zone.VOID;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class ChronologicalReversal extends Spell {
    
    UUID selected;

    public ChronologicalReversal(String owner) {
        super("Chronological Reversal", 1, new Hashmap(YELLOW, 1), 
                "Return target spell from scrapyard to your hand.\n" +
                "Purge ~", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) 
                && game.hasInstancesIn(controller, Spell.class, SCRAPYARD, 1);
    }
    
    @Override
    protected void beforePlay (Game game){
        targets = game.selectFromZone(controller, SCRAPYARD, Predicates::isSpell, 1, false);
        selected = targets.get(0);
    } 
    
    @Override
    protected void duringResolve (Game game){
        if(game.isIn(controller, selected, SCRAPYARD)){
            game.putTo(controller, game.extractCard(selected), HAND);
        }
    }
    
    @Override
    protected void afterResolve (Game game){
        game.putTo(controller, this, VOID);
    }    
}
