/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1.black;

import com.visitor.card.types.Spell;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA01 extends Spell {

    UUID target;
    
    public BA01(String owner) {
        super("BA01", 3, new Hashmap(BLACK, 2), "Possess target item that costs 3 or less.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasValidTargetsIn(controller, c->{return (c instanceof Item && c.cost <= 3);}, 1, "both play");
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, "both play", c->{return (c instanceof Item && c.cost <= 3);}, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolveEffect (Game game){
        if(game.isIn(controller, target, "both play")){
            game.possessTo(controller, target, "play");
        }
    }
}
