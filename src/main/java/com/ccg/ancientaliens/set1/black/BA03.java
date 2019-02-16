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
    
    /**
     *
     * @param owner
     */
    public BA03(String owner) {
        super("BA03", 1, new Hashmap(BLACK, 1), "Additional Cost - Sacrifice an item.<br>Draw 2 cards.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.players.get(controller).hasAnItem();
    }
    
    @Override
    public void play(Game game) {
        game.getTargetsFromPlay(this, 1);
        game.spendEnergy(controller, cost);
        game.destroy(((UUID[])supplementaryData)[0]);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        game.draw(controller, 2);
        game.putToScrapyard(this);
    }

    @Override
    public boolean validTarget(Card c) {
        return (c.controller.equals(controller) && c instanceof Item);
    }    
    
}
