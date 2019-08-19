/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Ally;
import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public class Coordination extends Ritual {
    
    public Coordination(String owner) {
        super("Coordination", 2, new Hashmap(GREEN, 2), 
        "Decrease Delay counter of each Ally you control by 1", owner);
    }

    @Override
    public void duringResolve (Game game){
        game.getAllFrom(controller, PLAY, Predicates::isAlly).forEach(c-> {((Ally)c).decreaseDelayCounter(game,1);});
    }
}

