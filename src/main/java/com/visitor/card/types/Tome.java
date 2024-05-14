package com.visitor.card.types;

import com.visitor.card.properties.Studiable;
import com.visitor.game.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Abstract class for the Tome card type.
 *
 * @author pseudo
 */
public abstract class Tome extends Card {

    public Tome(Game game, String name, String text, UUID owner) {
        super(game, name, new CounterMap<>(), CardType.Tome, text, owner);

        studiable = new Studiable(game, this);
    }

    public Tome(Game game, String name, String text, UUID owner, Supplier<CounterMap<Knowledge>> getKnowledgeType) {
        this(game, name, text, owner);

        studiable.setGetKnowledgeTypes(getKnowledgeType);
    }

    public Tome(Game game, String name, String text, UUID owner, CounterMap<Knowledge> studyKnowledgeType) {
        this(game, name, text, owner);

        studiable.setGetKnowledgeTypes(() -> studyKnowledgeType);
    }


    @Override
    public Types.CardP.Builder toCardMessage() {
        return super.toCardMessage()
                .setCost("");
    }
}