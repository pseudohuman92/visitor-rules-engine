/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Switcheroo extends Ritual{

    
    public Switcheroo(String owner) {
        super("Switcheroo", 1, new Hashmap(BLACK, 2), 
        "Each player chooses an Asset they control.\n" +
        "Donate the Asset you control and possess the Asset you don't control.", owner);
    }
    
    @Override
    protected void duringResolve (Game game){
        UUID contTarget = null;
        UUID oppTarget = null;
        if (game.hasIn(controller, PLAY, Predicates::isAsset, 1)){
            contTarget = game.selectFromZone(controller, PLAY, Predicates::isAsset, 1, false).get(0);
        }
        
        if (game.hasIn(game.getOpponentName(controller), PLAY, Predicates::isAsset, 1)){
            oppTarget = game.selectFromZone(game.getOpponentName(controller), PLAY, Predicates::isAsset, 1, false).get(0);
        }
        
        if (contTarget != null){
            game.donate(contTarget, game.getOpponentName(controller), PLAY);
        }
        
        if (oppTarget != null){
            game.possessTo(controller, oppTarget, PLAY);
        }
    }
}

