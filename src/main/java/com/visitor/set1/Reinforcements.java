/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.HAND;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Reinforcements extends Spell {

    UUID target; 
    
    public Reinforcements(String owner) {
        super("Reinforcements", 1, new Hashmap(RED, 1), 
            "Additional Cost \n" +
            "  Shuffle a card from your hand to your deck. \n" +
            "Draw two cards.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) && game.hasCardsIn(controller, HAND, 2);
    }
    
    @Override
    protected void beforePlay(Game game){
        target = game.selectFromZone(controller, HAND, c->{return !c.id.equals(id);}, 1, false).get(0);
        game.shuffleIntoDeck(controller, new Arraylist<>(game.extractCard(target)));
    }  
    
    @Override
    protected void duringResolve (Game game){
        game.draw(controller, 2);
    }    
}
