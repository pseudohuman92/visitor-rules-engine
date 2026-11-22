package com.visitor.sets.base2;

import com.visitor.card.types.Ritual;
import com.visitor.game.parts.Game;
import com.visitor.helpers.CounterMap;

import java.util.UUID;

import static com.visitor.protocol.Types.Knowledge.BLUE;

public class AbsoluteUnderstanding extends Ritual {
    public AbsoluteUnderstanding(Game game, UUID owner) {
        super(game, "Absolute Understanding", 6, new CounterMap<>(BLUE, 4),
                "Research 3. Draw 3 cards. Gain 3 energy this turn. "
                        + "You may play one card from your hand without paying its energy cost this turn. "
                        + "You must still satisfy its knowledge requirements and Research requirements.", owner);
        setResearchRequirement(3);
        playable.addResolveEffect(() -> {
            game.draw(controller, 3);
            game.addEnergy(controller, 3, true);
            game.addFreePlayCredit(controller);
        });
    }
}
