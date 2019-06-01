/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import static com.visitor.game.Game.Zone.DECK;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class PriorityOps extends Spell {

    UUID target; 
    
    public PriorityOps(String owner) {
        super("Priority Ops", 2, new Hashmap(RED, 2), 
                "Put target item on top of controller's deck.", owner);
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
            Card c = game.extractCard(target);
            c.clear();
            game.putTo(c.controller, c, DECK, 0);
        }
    }    
}
