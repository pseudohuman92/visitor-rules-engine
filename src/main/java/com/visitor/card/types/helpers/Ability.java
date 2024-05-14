/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.card.properties.Playable;
import com.visitor.game.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.protocol.Types;

import java.util.UUID;

/**
 * @author pseudo
 * Abilities are represenbted as "cards" that do their effect on resolution then disappear.
 */
public class Ability extends Card {

    public Ability(Game game, Card creator, String text, Runnable effect, Arraylist<UUID> targets) {
        super(game, creator.name + "'s Ability", new CounterMap<>(), CardType.Ability, text, creator.controller);
        this.targets = new Arraylist<>(creator.id).putAllIn(targets);

        playable = new Playable(game, this).addResolveEffect(effect).setDisappearing();

    }

    public Ability(Game game, Card creator, String text, Runnable effect, UUID... targets) {
        this(game, creator, text, effect, new Arraylist<>(targets));
    }

    public Ability(Game game, Card creator, ActivatedAbility activatedAbility) {
        this(game, creator, activatedAbility.getText(), activatedAbility.getActivate(), activatedAbility.getTargets());
        this.id = activatedAbility.id;
    }

    @Override
    public Types.CardP.Builder toCardMessage() {
        return super.toCardMessage()
                .setCost("");
    }

}
