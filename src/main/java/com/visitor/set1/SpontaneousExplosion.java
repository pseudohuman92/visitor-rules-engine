/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Junk;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import static com.visitor.game.Game.Zone.HAND;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class SpontaneousExplosion extends Spell {
    
    public SpontaneousExplosion(String owner) {
        super("Spontaneous Explosion", 1, new Hashmap(BLUE, 2), 
        "Destroy target Junk.\n" +
        "Its controller takes 3 damage.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){
        return super.canPlay(game) && game.hasInstancesIn(controller, Junk.class, BOTH_PLAY, 1);
    }
    
    @Override
    protected void beforePlay(Game game){
        targets = game.selectFromZone(controller, BOTH_PLAY, Predicates::isJunk, 1, false);
    }

    @Override
    protected void duringResolve (Game game){
        if (game.isIn(controller, targets.get(0), BOTH_PLAY)){
            Card c = game.getCard(targets.get(0));
            game.destroy(id, targets.get(0));
            game.dealDamage(id, game.getUserId(c.controller), 3);
        }
    }
}
