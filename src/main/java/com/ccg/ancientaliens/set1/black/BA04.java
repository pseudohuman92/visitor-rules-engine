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
import java.util.ArrayList;
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
            game.players.get(game.getOpponentName(controller)).hasInPlay(Item.class);
    }
    
    @Override
    public void resolve (Game game){
        ArrayList<UUID> selected = game.getSelectedFromPlay(game.getOpponentName(controller), this::validTarget, 1);
        game.possessFromPlay(controller, selected.get(0));
        game.putToScrapyard(this);
    }

    @Override
    public boolean validTarget(Card c) {
        return (!c.controller.equals(controller) && c instanceof Item);
    }    
    
}
