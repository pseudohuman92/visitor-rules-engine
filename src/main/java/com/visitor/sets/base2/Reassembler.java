/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.base2;

import com.visitor.card.properties.Damagable;
import com.visitor.card.types.Asset;
import com.visitor.card.Card;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.card.containers.ActivatedAbility;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.YELLOW;

/**
 * @author pseudo
 */
public class Reassembler extends Asset {

    public Reassembler(Game game, UUID owner) {
        super(game, "Reassembler",
                3, new CounterMap(YELLOW, 1),
                "{Use}: Purge one or more units from your discard pile: Target ally unit gains their total attack, health, shield and combat abilities.",
                owner);

        activatable.addActivatedAbility(new ActivatedAbility(game, this, 0, "{Use}: Purge one or more units from your discard pile: Target unit you control gains their total attack, health, shield and combat abilities.")
                .addTargeting(Base.Zone.Discard_Pile, Predicates::isUnit, 1, 999, "Purge any number of units.", game::purge, true)
                .addTargeting(Base.Zone.Play, Predicates.isAllyUnit(controller), 1, 1, "Target unit you control gains their total attack, health, shield and combat abilities.", id -> {}, false)
                .setStackEffectFactory(selections -> {
                    int totalAttack = 0;
                    int totalHealth = 0;
                    int totalShield = 0;
                    CounterMap<Damagable.CombatAbility> totalCombatAbilities = new CounterMap<>();
                    for (UUID id : selections.get(0)) {
                        Card card = game.getCard(id);
                        totalAttack += card.getAttack();
                        totalHealth += card.getHealth();
                        totalShield += card.getShields();
                        totalCombatAbilities.merge(card.getCombatAbilities());
                    }
                    UUID targetId = selections.get(1).get(0);
                    int attack = totalAttack;
                    int health = totalHealth;
                    int shield = totalShield;
                    return () -> {
                        Card target = game.getCard(targetId);
                        if (target != null && target.zone == Base.Zone.Play && Predicates.isAllyUnit(controller).test(target)) {
                            target.addAttackAndHealth(attack, health, false);
                            target.addShield(shield, false);
                            totalCombatAbilities.forEach((ability, count) -> target.addCombatAbility(ability, count, false));
                        }
                    };
                }), false);
    }
}
