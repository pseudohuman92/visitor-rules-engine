/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
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
                && game.hasInstancesIn(controller, Spell.class, "scrapyard", 1);
    }
    
    @Override
    public void play (Game game){
        targets = game.selectFromZone(controller, "scrapyard", Predicates::isSpell, 1, false);
        selected = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    } 
    
    @Override
    public void resolveEffect (Game game){}
    
    @Override
    public void resolve (Game game){
        if(game.isIn(controller, selected, "scrapyard")){
            game.putTo(controller, game.extractCard(selected), "hand");
        }
        game.putTo(controller, this, "void");
    }    
}
