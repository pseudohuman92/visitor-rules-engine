/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.card.properties.Playable;
import com.visitor.card.types.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

import java.util.UUID;

/**
 * @author pseudo
 */
public class AbilityCard extends Card {

    public AbilityCard(Game game, Card creator, String text, Runnable effect) {
        this(game, creator, text, effect, new Arraylist<>());
    }

    public AbilityCard(Game game, Card creator, String text, Runnable effect, UUID target) {
        this(game, creator, text, effect, new Arraylist<>(target));
    }

    public AbilityCard(Game game, Card creator, String text, Runnable effect, Arraylist<UUID> targets) {
        super(game, creator.name + "'s Ability", new Hashmap<>(), CardType.Ability, text, creator.controller);
        this.targets = new Arraylist<>(creator.id).putAllIn(targets);

        playable = new Playable(game, this).setResolveEffect(effect);
    }

    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setCost("");
    }

}
