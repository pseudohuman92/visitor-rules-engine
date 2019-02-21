/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.yellow;

import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.YELLOW;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA04 extends Action {

    int x; 
    
    public YA04(String owner) {
        super("YA04", 0, new Hashmap(YELLOW, 2), 
                "Recover X. \n" +
                "Purge ~.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) 
                && game.hasEnergy(controller, 2)
                && game.hasACardIn(controller, "scrapyard");
    }
    
    @Override
    public void play (Game game){
        x = game.selectX(controller, Math.min(game.players.get(controller).energy/2, 
                                              game.getZone(controller, "scrapyard").size()));
        game.spendEnergy(controller, 2 * x);
        game.addToStack(this);
    } 
    
    @Override
    public void resolve (Game game){
        ArrayList<UUID> selected = game.selectFromScrapyard(controller, c->{return true;}, x);
        ArrayList<Card> cards = game.extractAll(selected);
        game.shuffleIntoDeck(controller, cards);
        game.putTo(controller, this, "void");
    }    
}
