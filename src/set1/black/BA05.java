/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.black;

import card.types.Action;
import static enums.Knowledge.BLACK;
import static enums.Subtype.Trap;
import game.Game;
import helpers.Hashmap;

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
        super("BA05", 1, new Hashmap(BLACK, 1), "Look at the top 4 cards of your deck. Draw a trap among them. Put rest to the bottom.", "action.png", owner);
    }
    
    @Override
    public void resolve (Game game){
        game.selectFromTopOfDeck(controller, 4, 1, 
                (c) -> {return c.subtypes.contains(Trap);}, 
                (player, cards) -> { player.hand.addAll(cards);},
                (player, cards) -> { player.deck.putAllToBottom(cards);});
        game.discardAfterPlay(this);
    }
    
}
