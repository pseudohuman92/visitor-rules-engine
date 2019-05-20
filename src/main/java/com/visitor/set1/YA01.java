/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA01 extends Spell {

    UUID target; 
    
    public YA01(String owner) {
        super("YA01", 2, new Hashmap(YELLOW, 3), 
                "Gain reflect 2.\n" +
                "Shuffle ~ back into your deck.", owner);
    }
    
    @Override
    public void resolveEffect (Game game){}
    
    @Override
    public void resolve (Game game){
        game.addReflect(controller, 2);
        Arraylist<Card> tmp = new Arraylist<>();
        tmp.add(this);
        game.shuffleIntoDeck(controller, tmp);
    }    
}
