package com.visitor.sets.base2;

import com.visitor.card.Card;
import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class BloodAmmunition extends Cantrip {
    private int sacrificedAttack;

    public BloodAmmunition(Game game, UUID owner) {
        super(game, "Blood Ammunition", 1, new CounterMap<>(RED, 1).add(PURPLE, 1),
                "Additional Cost — Destroy an ally unit. Deal damage to any enemy equal to the destroyed unit's attack.", owner);
        playable.addTargetSingleUnit(Base.Zone.Play, Predicates.isAllyUnit(controller), id -> {
            Card unit = game.getCard(id);
            sacrificedAttack = unit.getAttack();
            game.destroy(id);
        }, "Destroy an ally unit as an additional cost.", true);
        playable.addTargetSingleCardOrPlayer(Base.Zone.Both_Play_With_Players,
                Predicates.isEnemyDamagable(controller), "Choose an enemy.",
                id -> game.dealDamage(getId(), id, sacrificedAttack), true, false);
    }
}
