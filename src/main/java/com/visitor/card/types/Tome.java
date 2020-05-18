package com.visitor.card.types;

import com.visitor.card.properties.Studiable;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;

import java.util.function.Supplier;

/**
 * Abstract class for the Tome card type.
 *
 * @author pseudo
 */
public abstract class Tome extends Card {

    public Tome(Game game, String name, String text, String owner) {
        super(game, name, new Hashmap<>(), CardType.Tome, text, owner);

        studiable = new Studiable(game, this);
    }

    public Tome(Game game, String name, String text, String owner, Supplier<Hashmap<Knowledge, Integer>> getKnowledgeType) {
        this(game, name, text, owner);

        studiable.setGetKnowledgeType(getKnowledgeType);
    }


    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setCost("");
    }
}