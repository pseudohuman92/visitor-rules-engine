/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
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
public class RA01 extends Spell {

    UUID target; 
    
    public RA01(String owner) {
        super("RA01", 2, new Hashmap(RED, 1), 
                "Return target item to controller's hand. \n" +
                "Deal 1 damage to opponent.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasInstancesIn(controller, Item.class, "both play", 1);
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, "both play", c->{return c instanceof Item;}, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolveEffect (Game game){
        if(game.isIn(controller, target, "both play")){
            Card c = game.extractCard(target);    
            game.putTo(c.controller, c, "hand");
        }
        game.damagePlayer(game.getOpponentName(controller), 1);
    }    
}