/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Card;
import com.visitor.card.types.Asset;
import com.visitor.card.types.Junk;
import com.visitor.game.Game;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.helpers.UUIDHelper.getInList;
import static com.visitor.helpers.UUIDHelper.getNotInList;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class QuantumStateExtractor extends Asset{
    
    public QuantumStateExtractor (String owner){
        super("Quantum State Extractor", 2, new Hashmap(BLUE, 2), 
                "1, Activate: Look at the top two cards of your deck. "
                + "Draw one, transform other into junk and shuffle into the deck.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game) && game.hasEnergy(controller, 1);
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.spendEnergy(controller, 1);
        game.addToStack(new Ability(this, 
                "Look at the top two cards of your deck. "
                + "Draw one, transform other into junk and shuffle into the deck.",
                (x) -> { 
                    Player p = game.getPlayer(controller);
                    Arraylist<Card> cand = p.deck.extractFromTop(2);
                    Arraylist<UUID> selected = game.selectFromList(controller, cand, Predicates::any, 1, false);
                    p.hand.addAll(getInList(cand, selected));
                    Card toJunk = getNotInList(cand, selected).get(0);
                    game.shuffleIntoDeck(controller, new Arraylist<>(toJunk));
                    game.transformToJunk(this, toJunk.id);
                }));
    }
}
