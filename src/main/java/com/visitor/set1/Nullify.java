/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import static com.visitor.game.Game.Zone.STACK;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Nullify extends Spell {

    UUID target; 
    
    public Nullify(String owner) {
        super("Nullify", 2, new Hashmap(YELLOW, 1), 
                "Cancel target spell.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) && game.hasInstancesIn(controller, Spell.class, STACK, 1);
    }
    
    @Override
    public void play (Game game){
        targets = game.selectFromZone(controller, STACK, Predicates::isSpell, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    } 
    
    @Override
    public void resolveEffect (Game game){
        Card c = game.extractCard(target);
        game.putTo(c.controller, c, SCRAPYARD);
    }    
}
