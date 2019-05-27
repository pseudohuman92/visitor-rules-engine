/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.game.Player;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class DataRecompiler extends Item{
    
    public DataRecompiler (String owner){
        super("Data Recompiler", 1, new Hashmap(BLUE, 1), 
                "1, Activate: Look at the top 2 cards of your library. "
                + "You may discard any number of them. Put the rest in the same order.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 1);
    }
    
    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.spendEnergy(controller, 1);
        game.addToStack(new Activation(controller, "Look at the top 2 cards of your library. "
                + "You may discard any number of them. Put the rest in the same order.",
                (x) -> { 
                    Player p = game.getPlayer(controller);
                    Arraylist<Card> cand = p.deck.extractFromTop(2);
                    Arraylist<UUID> selected = game.selectFromList(controller, cand, cx->{return true;}, 2, true);
                    p.deck.putToTop(UUIDHelper.getNotInList(cand, selected));
                    p.scrapyard.addAll(UUIDHelper.getInList(cand, selected));
                }));
    }
}
