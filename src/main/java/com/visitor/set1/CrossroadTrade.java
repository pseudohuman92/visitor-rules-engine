/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;


import com.visitor.card.types.Item;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class CrossroadTrade extends Spell{
    
    UUID target;
    
    public CrossroadTrade(String owner) {
        super("Crossroad Trade", 1, new Hashmap(BLACK, 1), 
        "Additional Cost - Sacrifice an item. Draw 2 cards.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasIn(controller, PLAY, Predicates::isItem, 1);
    }
    
    @Override
    protected void beforePlay(Game game) {
        target = game.selectFromZone(controller, PLAY, Predicates::isItem, 1, false).get(0);
        game.sacrifice(target);
    }
    
    @Override
    protected void duringResolve (Game game){
        game.draw(controller, 2);
    }   
}
