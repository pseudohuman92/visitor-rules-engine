/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.properties;

import com.visitor.card.types.helpers.AbilityCard;
import com.visitor.game.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;

import java.util.Arrays;
import java.util.UUID;

/**
 * Interface for cards that has an activating ability.
 *
 * @author pseudo
 */
public class Activatable {

    private final Card card;
    private final Game game;

    private Arraylist<ActivatedAbility> abilityList;
    private Arraylist<ActivatedAbility> turnlyAbilityList;


    // Constructors
    public Activatable(Game game, Card card) {
        this.game = game;
        this.card = card;
        abilityList = new Arraylist<>();
        turnlyAbilityList = new Arraylist<>();
    }

    public final boolean canActivate() {
        for (ActivatedAbility activatedAbility : abilityList) {
            if (activatedAbility.canActivate())
                return true;
        }
        for (ActivatedAbility activatedAbility : turnlyAbilityList) {
            if (activatedAbility.canActivate())
                return true;
        }
        return false;
    }

    public final Arraylist<ActivatedAbility> getActivatableAbilities() {
        Arraylist<ActivatedAbility> abilities = new Arraylist<>();
        for (ActivatedAbility activatedAbility : abilityList) {
            if (activatedAbility.canActivate())
                abilities.add(activatedAbility);
        }
        for (ActivatedAbility activatedAbility : turnlyAbilityList) {
            if (activatedAbility.canActivate())
                abilities.add(activatedAbility);
        }
        return abilities;
    }


    public final void activate() {
        Arraylist<ActivatedAbility> abilities = getActivatableAbilities();
        if (abilities.size() == 1) {
            abilities.get(0).activate();
        } else if (abilities.size() > 1) {
            Arraylist<Card> abilityCards = new Arraylist<>(abilities.transform(aa -> (Card) new AbilityCard(game, card, aa)));
            UUID chosenAbility = game.selectFromList(card.controller, abilityCards, Predicates::any, 1, false, "Choose an ability to activate.").get(0);
            abilities.forEach(aa -> {
                if (aa.id.equals(chosenAbility)) {
                    aa.activate();
                }
            });
        }
    }

    // Adders
    public Activatable addActivatedAbility(ActivatedAbility... abilities) {
        abilityList.addAll(Arrays.asList(abilities));
        return this;
    }

    public void addTurnlyActivatedAbility(ActivatedAbility... abilities) {
        turnlyAbilityList.addAll(Arrays.asList(abilities));
    }

    public void endTurn() {
        turnlyAbilityList.clear();
    }
}
