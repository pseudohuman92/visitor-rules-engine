package com.visitor.card.types;

import com.visitor.card.properties.Playable;
import com.visitor.card.properties.Studiable;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;


/**
 * Abstract class for the Spell card type.
 *
 * @author pseudo
 */
public abstract class Spell extends Card {

    public Spell(Game game, String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String owner) {
        super(game, name, knowledge, CardType.Spell, text, owner);

        playable = new Playable(game, this, cost).setFast().setEphemeral();
        studiable = new Studiable(game, this);
    }
}
