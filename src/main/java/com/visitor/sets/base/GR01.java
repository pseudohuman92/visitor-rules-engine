package com.visitor.sets.base;

import com.visitor.card.Card;
import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.Damage;

import java.util.UUID;

import static com.visitor.game.Game.Zone.*;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.GREEN;

public class GR01 extends Ritual {

    public GR01(Game game, String owner) {
        super(game, "UR01", 2,
                new CounterMap<>(GREEN, 1),
            "Target unit you control strikes target unit you don't control.",
                owner);

        playable
        .setCanPlayAdditional(() ->
            game.hasIn(playable.card.controller, Play, Predicates::isUnit, 1) &&
            game.hasIn(playable.card.controller, Opponent_Play, Predicates::isUnit, 1)
        )
        .setBeforePlay(() -> {
            targets.add(game.selectFromZone(playable.card.controller, Play, Predicates::isUnit, 1, false).get(0));
            targets.add(game.selectFromZone(playable.card.controller, Opponent_Play, Predicates::isUnit, 1, false).get(0));
        })
        .setResolveEffect(() -> {
            Card striker = game.getCard(targets.get(0));
            Card receiver = game.getCard(targets.get(1));
            receiver.receiveDamage(new Damage(striker.getAttack()), striker);
        });
    }
}
