/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.black;


import com.ccg.ancientaliens.card.properties.Targeting;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA04 extends Action implements Targeting {
    
    /**
     *
     * @param owner
     */
    public BA04(String owner) {
        super("BA04", 2, new Hashmap(BLACK, 2), 
        "Opponent chooses an item he controls, posses that item.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && 
            game.hasInstancesIn(game.getOpponentName(controller), Item.class, "play", 1);
    }
    
    @Override
    public void resolve (Game game){
        Arraylist<UUID> selected = game.selectFromZone(game.getOpponentName(controller), "play", this::validTarget, 1, false);
        game.possessTo(controller, selected.get(0), "play");
        game.putTo(controller, this, "scrapyard");
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item);
    }    
    
}
