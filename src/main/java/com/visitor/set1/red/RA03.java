/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1.red;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class RA03 extends Spell {

    UUID target; 
    
    public RA03(String owner) {
        super("RA03", 1, new Hashmap(RED, 2), 
                "Opponent purges 2. \n" +
                "Shuffle ~ to your deck.", owner);
    }
    
    @Override
    public void resolveEffect (Game game){}
    
    @Override
    public void resolve (Game game){
        game.damagePlayer(game.getOpponentName(controller), 2);
        game.shuffleIntoDeck(controller, new Arraylist<>(this));
    }    
}
