/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;

import com.visitor.card.types.helpers.Ability;
import com.visitor.card.types.helpers.TriggeringPassive;
import com.visitor.game.Event;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import static com.visitor.game.Event.playersTurnStart;

/**
 *
 * @author pseudo
 */
public class CreepingPoison extends TriggeringPassive {

    public CreepingPoison(String owner) {
        super("Creeping Poison", 3, new Hashmap(BLACK, 2), 
                "Donate\n" +
                "Trigger - At the start of your turn\n" +
                "    Deal 1 damage to your controller.", owner);
    }
    

    @Override
    public void checkEvent(Game game, Event event) {
        if (playersTurnStart(event, controller)){
            game.addToStack(new Ability(this,
            "Deal 1 damage to your controller.",
            (x) -> {
                game.dealDamage(id, game.getUserId(controller), 1);
            }, game.getUserId(controller)));
        }
    }
    
    @Override
    protected void afterResolve(Game game) {
        game.addToStack(new Ability(this,
        "Donate",
        a -> {
            game.donate(id, game.getOpponentName(controller), PLAY);
        }
        ));
    }
}
