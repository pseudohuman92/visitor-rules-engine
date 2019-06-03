/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public class Resurrection extends Ritual {

    public Resurrection(String owner) {
        super("Resurrection", 3, new Hashmap(GREEN, 2), 
        "Return target Ally from your scrapyard to play with 1 health.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){
        return super.canPlay(game) && game.hasValidTargetsIn(controller, Predicates::isAlly, 1, SCRAPYARD);
    }
    
    @Override
    protected void beforePlay(Game game){
        targets = game.selectFromZone(controller, SCRAPYARD, Predicates::isAlly, 1, false);
    }

    @Override
    protected void duringResolve (Game game){
        if (game.isIn(controller, targets.get(0), SCRAPYARD)){
            Card c = game.extractCard(targets.get(0));
            c.health = 1;
            game.putTo(controller, c, PLAY);
        }
    }
    
}

