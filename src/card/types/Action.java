
package card.types;

import card.Card;
import enums.Knowledge;
import game.Game;
import helpers.Hashmap;

/**
 * Abstract class for the Action card type.
 * @author pseudo
 */
public abstract class Action extends Card {
    
    /**
     *
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Action(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, "assets/action.png", owner);
    }

    @Override
    public boolean canPlay(Game game){ 
        return (game.players.get(controller).energy >= cost)
               && game.players.get(controller).hasKnowledge(knowledge);
    }
}
