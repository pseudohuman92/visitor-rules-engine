/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Card;
import static com.ccg.ancientaliens.enums.Subtype.Trap;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.game.Player;
import com.ccg.ancientaliens.helpers.UUIDHelper;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BA05 extends Action {
    
    /**
     *
     * @param owner
     */
    public BA05(String owner) {
        super("BA05", 1, new Hashmap(BLACK, 1), 
        "Look at the top 4 cards of your deck. Draw a trap among them. Put rest to the bottom.", owner);
    }
    
    @Override
    public void resolve (Game game){
        Player p = game.players.get(controller);
        ArrayList<Card> topCards = p.deck.extractFromTop(4);
        ArrayList <UUID> canSelected = new ArrayList<>();
        topCards.forEach(c -> {
            if (c.subtypes.contains(Trap)){
                canSelected.add(c.id);
            }
        });
        ArrayList<UUID> s = game.getSelectedFromList(controller, topCards, canSelected, 1);
        ArrayList<Card> selected = UUIDHelper.getInList(topCards, s);
        ArrayList<Card> notSelected = UUIDHelper.getNotInList(topCards, s);
        p.hand.addAll(selected);
        p.deck.putAllToBottom(notSelected);
        game.putToScrapyard(this);
    }
    
}
