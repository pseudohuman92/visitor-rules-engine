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
public class AL01 extends Ally {
    
    public AL01(UL01 c){
        super ("AL01", 2, new Hashmap(BLUE, 1),
            "-2 Loyalty, Activate: \n" +
            "  Favor 1 - Return target Item from your scrapyard to play.\n" +
            "If ~ has no loyalty, Transform it to UL01", 5,
            c.controller);
        copyPropertiesFrom(c);
    }
        
    @Override
    public boolean canActivate(Game game){
        return super.canActivate(game) && 
                    game.hasInstancesIn(controller, Item.class, "scrapyard", 1) && 
                loyalty >= 2; 
    }
    

    @Override
    public void activate(Game game) {

        UUID target = game.selectFromZone(controller, "scrapyard", Predicates::isItem, 1, false).get(0);
        depleted = true;
        loyalty -=2;
        favor = 1;
        favorAbility =  new Ability(this, 
                "Return target Item from your scrapyard to play.\n" +
                  "If ~ has no loyalty, Transform it to UL01",
            (x2) -> {
                if(game.isIn(controller, target, "scrapyard")){

                game.putTo(controller, game.extractCard(target), "play");
                }
                if (loyalty == 0){
                    game.replaceWith(this, new UL01(this));
                }
            }, new Arraylist<>(target));
    }
    
}

