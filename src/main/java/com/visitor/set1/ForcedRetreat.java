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
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class ForcedRetreat extends Spell {

    UUID target; 
    
    public ForcedRetreat(String owner) {
        super("Forced Retreat", 2, new Hashmap(RED, 1), 
                "Return target item to controller's hand. \n" +
                "Deal 1 damage to opponent.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasInstancesIn(controller, Item.class, BOTH_PLAY, 1);
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, BOTH_PLAY, Predicates::isItem, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolveEffect (Game game){
        if(game.isIn(controller, target, BOTH_PLAY)){
            game.getCard(target).returnToHand(game);
        }
        game.damagePlayer(id, game.getOpponentName(controller), 1);
    }    
}
