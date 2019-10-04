/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Ritual;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import static com.visitor.game.Game.Zone.HAND;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Recombobulate extends Ritual {
    
    public Recombobulate(String owner) {
        super("Recombobulate", 4, new Hashmap(BLUE, 3), 
        "Shuffle all junk from your hand into your deck and draw that many cards.", owner);
    }

    @Override
    protected void duringResolve (Game game){
        Arraylist<Card> junks = game.getAllFrom(controller, HAND, Predicates::isJunk);
        game.shuffleIntoDeck(controller, junks);
        game.draw(controller, junks.size());
    }
}
