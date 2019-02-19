/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.properties.Targeting;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import helpers.Hashmap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA01 extends Action implements Targeting {

    public BA01(String owner) {
        super("BA01", 3, new Hashmap(BLACK, 2), "Possess target item that costs 3 or less.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasValidTargetsInPlay(this, 1);
    }
    
    @Override
    public void play(Game game) {
        game.getSelectedFromPlay(controller, this::validTarget, 1);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        game.possess(supplementaryData.get(0), controller);
        game.putToScrapyard(this);
    }

    @Override
    public boolean validTarget(Card c) {
        return (c instanceof Item && c.cost <= 3);
    }
    
    
}
