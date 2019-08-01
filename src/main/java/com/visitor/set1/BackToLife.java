/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.HAND;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public class BacktoLife extends Spell {

    public BacktoLife(String owner) {
        super("Back To Life", 2, new Hashmap(GREEN, 1), 
        "Return target Ally from your scrapyard to your hand.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){
        return super.canPlay(game) && game.hasIn(controller, SCRAPYARD, Predicates::isAlly, 1);
    }
    
    @Override
    protected void beforePlay(Game game){
        targets = game.selectFromZone(controller, SCRAPYARD, Predicates::isAlly, 1, false);
    }

    @Override
    protected void duringResolve (Game game){
        if (game.isIn(controller, targets.get(0), SCRAPYARD)){
            Card c = game.extractCard(targets.get(0));
            game.putTo(controller, c, HAND);
        }
    }
    
}

