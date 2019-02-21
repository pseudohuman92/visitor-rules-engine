/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.game.Player;
import com.ccg.ancientaliens.helpers.UUIDHelper;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class UI03 extends Item{
    
    public UI03 (String owner){
        super("UI03", 1, new Hashmap(BLUE, 1), 
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
                (g , c) -> { 
                    Player p = g.getPlayer(c.controller);
                    ArrayList<Card> cand = p.deck.extractFromTop(2);
                    ArrayList<UUID> selected = g.selectFromListUpTo(controller, cand, UUIDHelper.toUUIDList(cand), 2);
                    p.deck.putToTop(UUIDHelper.getNotInList(cand, selected));
                    p.scrapyard.addAll(UUIDHelper.getInList(cand, selected));
                }));
    }
}
