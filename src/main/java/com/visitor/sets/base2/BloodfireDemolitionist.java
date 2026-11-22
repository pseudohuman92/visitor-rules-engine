package com.visitor.sets.base2;

import com.visitor.card.containers.ActivatedAbility;
import com.visitor.card.types.Unit;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.PURPLE;
import static com.visitor.protocol.Types.Knowledge.RED;

public class BloodfireDemolitionist extends Unit {
    public BloodfireDemolitionist(Game game, UUID owner) {
        super(game, "Bloodfire Demolitionist", 3, new CounterMap<>(RED, 2).add(PURPLE, 1),
                "{1}, Destroy another ally unit: Deal 2 damage to any enemy.", 3, 2, owner);
        activatable.addActivatedAbility(new ActivatedAbility(game, this, 1, text)
                .addTargeting(Base.Zone.Play, c -> Predicates.isAllyUnit(controller).test(c) && Predicates.isUnit(c)
                        && !c.getId().equals(getId()), 1, 1, "Destroy another ally unit.", game::destroy, true)
                .addTargeting(Base.Zone.Both_Play_With_Players, Predicates.isEnemyDamagable(controller),
                        1, 1, "Choose an enemy.", id -> game.dealDamage(getId(), id, 2), false));
    }
}
