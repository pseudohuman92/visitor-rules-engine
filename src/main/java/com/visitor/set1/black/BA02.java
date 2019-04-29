/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1.black;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

/**
 *
 * @author pseudo
 */
public class BA02 extends Spell {
    
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
        text = targetPlayer + " loots " + x;
        game.addToStack(this);
    }
    
    @Override
    public void resolveEffect (Game game){
        game.loot(targetPlayer, cost);
        text = "Target player loots X.";
        cost = 0;
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage().setCost("X");
    }
}
