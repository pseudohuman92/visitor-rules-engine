/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.storage;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.game.Player;
import com.ccg.ancientaliens.helpers.UUIDHelper;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;
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
        Player p = game.getPlayer(controller);
        Arraylist<Card> topCards = p.deck.extractFromTop(4);
        Arraylist<UUID> s = game.selectFromList(controller, topCards, c->{return c.subtypes.contains("Trap");}, 1, true);
        Arraylist<Card> selected = UUIDHelper.getInList(topCards, s);
        Arraylist<Card> notSelected = UUIDHelper.getNotInList(topCards, s);
        p.hand.addAll(selected);
        p.deck.putToBottom(notSelected);
        game.putTo(controller, this, "scrapyard");
    }
    
}
