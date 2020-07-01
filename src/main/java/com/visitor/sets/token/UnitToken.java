package com.visitor.sets.token;

import com.visitor.card.properties.Combat;
import com.visitor.card.types.specialtypes.ActivatableUnit;
import com.visitor.game.Game;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.protocol.Types;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;
import static com.visitor.card.properties.Combat.CombatAbility.Flying;
import static com.visitor.protocol.Types.Knowledge.PURPLE;

public class UnitToken extends ActivatableUnit {
	public UnitToken (Game game, String name, CardSubtype subtype, int cost, CounterMap<Types.Knowledge> knowledge, String text, int attack, int health, String owner, Combat.CombatAbility... combatAbilities) {
		super(game, name, cost, knowledge, text, attack, health, owner, combatAbilities);
		subtypes.add(subtype);
	}

	public static UnitToken Zombie_2_2 (Game game, String owner) {
		return new UnitToken(game, "Zombie", CardSubtype.Zombie, 2, new CounterMap<>(PURPLE, 1), "", 2, 2, owner);
	}

	public static UnitToken Bat_1_1 (Game game, String owner) {
		return new UnitToken(game, "Bat", CardSubtype.Bat, 1, new CounterMap<>(PURPLE, 1), "", 1, 1, owner, Flying);
	}
}


