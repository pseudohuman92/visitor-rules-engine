/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import com.visitor.helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public class YA06 extends Spell {

    public YA06(String owner) {
        super("YA06", 3, new Hashmap(YELLOW, 2), 
                "Each player draws 3 cards", owner);
    }
    
    @Override
    public void resolveEffect (Game game){
        game.draw(controller, 3);
        game.draw(game.getOpponentName(controller), 3);
    }    
}
