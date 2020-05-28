/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.properties;

import com.visitor.card.Card;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;

/**
 * Interface for cards that has an activating ability.
 *
 * @author pseudo
 */
public class Activatable {

    private final Card card;
    private final Game game;

    private Arraylist<ActivatedAbility> abilityList;

    // Constructors
    public Activatable(Game game, Card card) {
        this.game = game;
        this.card = card;
        abilityList = new Arraylist<>();
    }

    public Activatable(Game game, Card card, ActivatedAbility ability) {
        this(game, card);
        addActivatedAbility(ability);
    }

    public final boolean canActivate(){
        for (int i = 0; i < abilityList.size(); i++) {
            if (abilityList.get(i).canActivate.get())
                return true;
        }
        return false;
    }

    public final Arraylist getActivatableAbilities() {
        Arraylist<ActivatedAbility> abilities = new Arraylist<>();
        for (int i = 0; i < abilityList.size(); i++) {
            if (abilityList.get(i).canActivate.get())
                abilities.add(abilityList.get(i));
        }
        return abilities;
    }


    public final void activate() {
        Arraylist<ActivatedAbility> abilities = getActivatableAbilities();
        if (abilities.size() == 1){
            abilities.get(0).activate.run();
        } else if (abilities.size() > 1){
            // TODO: figure this out
        }
    }

    // Adders
    public Activatable addActivatedAbility(ActivatedAbility ability) {
        abilityList.add(ability);
        return this;
    }
    // Resetters

    public final void resetAbilityList() {
        abilityList.clear();
    }
}
