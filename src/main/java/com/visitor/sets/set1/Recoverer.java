/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;


import com.visitor.card.types.helpers.Ability;
import com.visitor.card.types.Ally;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Recoverer extends Ally {
    
    public Recoverer(ShadyTrader c){
        super ("Recoverer", 2, new Hashmap(BLUE, 1),
            "-2 Loyalty, Activate: \n" +
            "  Delay 1 - Return target Asset from your scrapyard to play.\n" +
            "If ~ has no loyalty, Transform it to Shady Trader", 5,
            c.controller);
        copyPropertiesFrom(c);
    }
        
    @Override
    public boolean canActivateAdditional(Game game){
        return   
                    game.hasIn(controller, SCRAPYARD, Predicates::isAsset, 1) && 
                loyalty >= 2; 
    }
    

    @Override
    public void activate(Game game) {

        UUID target = game.selectFromZone(controller, SCRAPYARD, Predicates::isAsset, 1, false).get(0);
        game.deplete(id);
        loyalty -=2;
        delayCounter = 1;
        delayedAbility =  new Ability(this, 
                "Return target Asset from your scrapyard to play.\n" +
                  "If ~ has no loyalty, Transform it to UL01",
            (x2) -> {
                if(game.isIn(controller, target, SCRAPYARD)){

                game.putTo(controller, game.extractCard(target), PLAY);
                }
                if (loyalty == 0){
                    game.transformTo(this, this, new ShadyTrader(this));
                }
            }, new Arraylist<>(target));
    }
    
}

