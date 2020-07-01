package com.visitor.card.types.specialtypes;

import com.visitor.card.properties.Activatable;
import com.visitor.card.properties.Combat;
import com.visitor.card.types.Asset;
import com.visitor.card.types.Unit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.protocol.Types;

public class ActivatableUnit extends Unit {
	public ActivatableUnit (Game game, String name, int cost, CounterMap<Types.Knowledge> knowledge, String text, int attack, int health, String owner, Combat.CombatAbility...combatAbilities) {
		super(game, name, cost, knowledge, text, attack, health, owner, combatAbilities);
		activatable = new Activatable(game, this);
	}
}
