package com.visitor.card.types.specialtypes;

import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Passive;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types.Knowledge;

/**
 * Abstract class for the Passive card type.
 *
 * @author pseudo
 */
public abstract class TriggeringPassive extends Passive {

    /**
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public TriggeringPassive(Game game, String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(game, name, cost, knowledge, text, owner);

        playable.setResolveEffect(() -> triggering.register());
        triggering = new Triggering(game, this);
    }

}
