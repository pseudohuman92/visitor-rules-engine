/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.testset;

import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;
import static com.visitor.card.properties.Combat.CombatAbility.Vigilance;

/**
 * @author pseudo
 */
public class Vigilanty extends Unit {

    public Vigilanty(Game game, String owner) {
        super(game, "Vigilanty",
                0, new Hashmap(),
                "",
                1, 4,
                owner);
        combat.addCombatAbility(Vigilance);
    }
}
