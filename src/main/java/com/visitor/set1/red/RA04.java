/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1.red;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class RA04 extends Spell {

    UUID target; 
    
    public RA04(String owner) {
        super("RA04", 1, new Hashmap(RED, 1), 
            "Additional Cost \n" +
            "  Shuffle a card from your hand to your deck. \n" +
            "Draw two cards.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) && game.hasCardsIn(controller, "hand", 2);
    }
    
    @Override
    public void play (Game game){
        target = game.selectFromZone(controller, "hand", c->{return !c.id.equals(id);}, 1, false).get(0);
        game.shuffleIntoDeck(controller, new Arraylist<>(game.extractCard(target)));
        super.play(game);
    }  
    
    @Override
    public void resolveEffect (Game game){
        game.draw(controller, 2);
    }    
}
