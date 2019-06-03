/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import com.visitor.helpers.Hashmap;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public class ChangingSides extends Ritual {
    
    int x;
    
    public ChangingSides(String owner) {
        super("Changing Sides", 4, new Hashmap(GREEN, 2), 
        "Possess target ally with no loyalty counters on it. \n" +
        "Place 1 loyalty on possessed ally.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){
        return super.canPlay(game) && game.hasValidTargetsIn(controller, ChangingSides::validTarget, 1, BOTH_PLAY);
    }
    
    @Override
    protected void beforePlay(Game game){
        targets = game.selectFromZone(controller, BOTH_PLAY, ChangingSides::validTarget, 1, false);
    }

    @Override
    protected void duringResolve (Game game){
        if (game.isIn(controller, targets.get(0), BOTH_PLAY)){
            game.possessTo(controller, targets.get(0), BOTH_PLAY);
        }
    }
    
    public static boolean validTarget(Card c){
        return c instanceof Ally && 
                ((Ally)c).loyalty == 0;
    }
}

