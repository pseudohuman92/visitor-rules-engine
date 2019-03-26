/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.red;

import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Arraylist;
import com.ccg.ancientaliens.helpers.Hashmap;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;
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
    public void resolve (Game game){
        game.draw(controller, 2);
        game.putTo(controller, this, "scrapyard");
    }    
}
