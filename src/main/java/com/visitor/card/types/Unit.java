package com.visitor.card.types;

import com.visitor.card.properties.*;
import com.visitor.card.Card;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types.Knowledge;

import java.util.UUID;

/**
 * Abstract class for the Asset card type.
 *
 * @author pseudo
 */
public abstract class Unit extends Card {

    public Unit(Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, int attack, int health, int shield, UUID owner, Damagable.CombatAbility... combatAbilities) {
        super(game, name, knowledge, CardType.Unit, text, owner);
        playable = new Playable(game, this, cost, () -> damagable.setDeploying()).setSlow().setPersistent();
        studiable = new Studiable(game, this);
        activatable = new Activatable(game, this);
        triggering = new Triggering(game, this);
        damagable = new Damagable(game, this, attack, health, shield);
        for (Damagable.CombatAbility combatAbility : combatAbilities) {
            damagable.addCombatAbility(combatAbility, false);
        }
    }

    public Unit(Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, int attack, int health, UUID owner, Damagable.CombatAbility... combatAbilities) {
        this(game, name, cost, knowledge, text, attack, health, 0, owner, combatAbilities);
    }

}
