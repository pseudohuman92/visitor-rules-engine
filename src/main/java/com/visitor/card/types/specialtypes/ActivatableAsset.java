package com.visitor.card.types.specialtypes;

import com.visitor.card.properties.Activatable;
import com.visitor.card.types.Asset;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.Types;

public class ActivatableAsset extends Asset {
    public ActivatableAsset(Game game, String name, int cost, CounterMap<Types.Knowledge> knowledge, String text, String owner) {
        super(game, name, cost, knowledge, text, owner);
        activatable = new Activatable(game, this);
    }
}
