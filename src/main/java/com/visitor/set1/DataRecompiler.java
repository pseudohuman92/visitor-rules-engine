/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Card;
import com.visitor.card.types.Asset;
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
public class DataRecompiler extends Asset{
    
    public DataRecompiler (String owner){
        super("Data Recompiler", 1, new Hashmap(BLUE, 1), 
                "1, Activate: Look at the top 2 cards of your library. "
                + "You may discard any number of them. Put the rest in the same order.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game) && game.hasEnergy(controller, 1);
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.spendEnergy(controller, 1);
        game.addToStack(new Ability(this, "Look at the top 2 cards of your library. "
                + "You may discard any number of them. Put the rest in the same order.",
                (x) -> { 
                    Player p = game.getPlayer(controller);
                    Arraylist<Card> cand = p.deck.extractFromTop(2);
                    Arraylist<UUID> selected = game.selectFromList(controller, cand, Predicates::any, 2, true);
                    p.deck.putToTop(getNotInList(cand, selected));
                    p.scrapyard.addAll(getInList(cand, selected));
                }));
    }
}
