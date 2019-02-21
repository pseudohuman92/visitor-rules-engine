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
import helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA03 extends Action implements Targeting {
    
    UUID target;
    
    public BA03(String owner) {
        super("BA03", 1, new Hashmap(BLACK, 1), 
        "Additional Cost - Sacrifice an item.<br>Draw 2 cards.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasAnInstanceIn(controller, Item.class, "play");
    }
    
    @Override
    public void play(Game game) {
        target = game.selectFromPlay(controller, this::validTarget, 1).get(0);
        game.spendEnergy(controller, cost);
        game.destroy(target);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        game.draw(controller, 2);
        game.putTo(controller, this, "scrapyard");
    }

    @Override
    public boolean validTarget(Card c) {
        return (c.controller.equals(controller) && c instanceof Item);
    }    
    
}
