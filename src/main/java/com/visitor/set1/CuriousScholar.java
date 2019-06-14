/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;


import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Ability;
import com.visitor.card.types.Ally;
import com.visitor.game.Event;
import static com.visitor.game.Event.EventType.STUDY;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.DECK;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.GREEN;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class CuriousScholar extends Ally implements Triggering {
    
    public CuriousScholar(String owner){
        super ("Curious Scholar", 2, new Hashmap(GREEN, 1),
            "Trigger - When you study\n" +
            "    +1 Loyalty\n" +
            "-2 Loyalty, Activate: \n" +
            "    Favor 1 - Search your deck for a Green card and draw it", 
            3,
            owner);
    }
    
        
    @Override
    public boolean canActivate(Game game){
        return super.canActivate(game) && loyalty >= 2; 
    }
    
    @Override
    public void checkEvent(Game game, Event e){
        if (e.type == STUDY && ((String) e.data.get(0)).equals(controller)){
            game.addToStack(new Ability(this, "Add 1 Loyalty", 
            a -> {
                loyalty += 1;
            }));
        }
        super.checkEvent(game, e);
    }
    

    @Override
    public void activate(Game game) {
        loyalty -= 2;
        game.deplete(id);
        favor = 1;
        favorAbility = new Ability(this, "Search your deck for a Green card and draw it", 
        a -> {
            UUID target = game.selectFromList(controller, game.getZone(controller, DECK), Predicates::isGreen, 1, false).get(0);
            game.draw(controller, target);
        });
    }
    
}

