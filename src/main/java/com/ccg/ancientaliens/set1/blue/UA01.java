/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class UA01 extends Action {

    UUID target; 
    
    public UA01(String owner) {
        super("UA01", 1, new Hashmap(BLUE, 2), "Purge all copies of a target card from your scrapyard. Opponent Purge as many cards.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasACardIn(controller, "scrapyard");
    }
    
    @Override
    public void play(Game game) {
        target = game.selectFromScrapyard(controller, c->{return true;}, 1).get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        if(game.isIn(controller, target, "scrapyard")){
            Card c = game.getCard(target);
            ArrayList<Card> cards = game.extractAllCopiesFrom(controller, c.name, "scrapyard");
            game.putAllTo(controller, cards, "void");
            game.purge(game.getOpponentName(controller), cards.size());
        }
        game.putTo(controller, this, "scrapyard");
    }    
}
