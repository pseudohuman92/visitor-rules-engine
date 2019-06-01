/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;


import com.visitor.card.types.Ability;
import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class UL01 extends Ally {
    
    public UL01(String owner){
        super ("UL01", 1, new Hashmap(BLUE, 1),
            "Discard an Item, Activate: +3 Loyalty\n" +
            "If ~ has 6 or more loyalty, Transform it to AL01", 5,
            owner);
    }
    
    public UL01(AL01 c){
        super ("UL01", 1, new Hashmap(BLUE, 1),
            "Discard an Item, Activate: +3 Loyalty\n" +
            "If ~ has 6 or more loyalty, Transform it to AL01", 5,
            c.controller);
        copyPropertiesFrom(c);
    }
        
    @Override
    public boolean canActivate(Game game){
        return super.canActivate(game) && 
                    game.hasInstancesIn(controller, Item.class, "hand", 1); 
    }
    

    @Override
    public void activate(Game game) {
        UUID target = game.selectFromZone(controller, "hand", Predicates::isItem, 1, false).get(0);
        game.discard(controller, target);
        depleted = true;
        loyalty +=3;
        if(loyalty >= 6){
            game.replaceWith(this, new AL01(this));
        }
    }
    
}

