package com.visitor.sets.base2;

import com.visitor.card.types.Unit;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.Evasive;
import static com.visitor.protocol.Types.Knowledge.BLUE;

public class ParallaxEntity extends Unit {
    public ParallaxEntity(Game game, UUID owner) {
        super(game, "Parallax Entity", 6, new CounterMap<>(BLUE, 4),
                "Research 3. When Parallax Entity enters play, draw a card for every 2 knowledge you have.",
                6, 6, owner, Evasive);
        setResearchRequirement(3);
        playable.addAfterResolveEffect(() -> game.draw(controller, game.getTotalKnowledge(controller) / 2));
    }
}
