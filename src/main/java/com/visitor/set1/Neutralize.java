/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Spell;
import com.visitor.card.types.Ability;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import com.visitor.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Neutralize extends Spell {

    UUID target; 
    
    public Neutralize(String owner) {
        super("Neutralize", 3, new Hashmap(YELLOW, 2), 
                "Cancel target card.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) && game.hasValidTargetsIn(controller, c->{return !(c instanceof Ability);}, 1, "stack");
    }
    
    @Override
    public void play (Game game){
        targets = game.selectFromZone(controller, "stack", c->{return !(c instanceof Ability);}, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    } 
    
    @Override
    public void resolveEffect (Game game){
        Card c = game.extractCard(target);
        game.putTo(c.controller, c, "scrapyard");
    }    
}
