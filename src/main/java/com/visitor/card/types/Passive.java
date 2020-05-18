package com.visitor.card.types;

import com.visitor.card.properties.Playable;
import com.visitor.card.properties.Studiable;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;

/**
 * Abstract class for the Passive card type.
 *
 * @author pseudo
 */
public abstract class Passive extends Card {

    /**
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Passive(Game game, String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(game, name, knowledge, CardType.Passive, text, owner);

        playable = new Playable(game, this, cost).setSlow().setPersistent();
        studiable = new Studiable(game, this);
    }
}
