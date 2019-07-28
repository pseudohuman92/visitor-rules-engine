/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Deck;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.DECK;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public class Energize extends Spell {
    
    public Energize(String owner) {
        super("Energize", 3, new Hashmap(GREEN, 1), 
        "Study the top studiable card of your deck", owner);
    }

    @Override
    protected void duringResolve (Game game){
        game.studyCardIrregular(controller, ((Deck)game.getZone(controller, DECK)).getTopmost(Predicates::isStudyable).id);
    }
}

