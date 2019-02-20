/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.properties.Targeting;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public class UA02 extends Action implements Targeting {

    public UA02(String owner) {
        super("UA02", 3, new Hashmap(BLUE, 1), "Transform target item into Junk", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasAnInstanceIn(controller, Item.class, "both play");
    }
    
    @Override
    public void play(Game game) {
        supplementaryData = game.getSelectedFromPlay(controller, this::validTarget, 1);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        if(game.isIn(controller, supplementaryData.get(0), "both play")){
            game.transformToJunk(supplementaryData.get(0));
        }
        game.putTo(controller, this, "scrapyard");
    }    

    @Override
    public boolean validTarget(Card c) {
        return c instanceof Item;
    }
}
