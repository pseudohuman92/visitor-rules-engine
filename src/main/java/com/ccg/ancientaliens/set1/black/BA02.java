/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.properties.XValued;
import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;

/**
 *
 * @author pseudo
 */
public class BA02 extends Action implements XValued {
    
    String targetPlayer;
    
    public BA02(String owner) {
        super("BA02", 0, new Hashmap(BLACK, 1), "Target player loots X.", owner);
    }
    

    @Override
    public void play(Game game) {
        targetPlayer = game.selectPlayer(controller);
        int x = game.selectX(controller, game.getPlayer(controller).energy);
        cost = x;
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        game.loot(targetPlayer, cost);
        game.putTo(controller, this, "scrapyard");
    }
    
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage().setCost("X");
    }
}
