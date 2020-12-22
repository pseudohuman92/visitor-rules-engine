package com.visitor.card.types;

import com.visitor.card.properties.*;
import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.protocol.Types.Knowledge;

import static com.visitor.game.Game.Zone.Hand;

/**
 * Abstract class for the Asset card type.
 *
 * @author pseudo
 */
public abstract class Unit extends Card {

	public Unit (Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, int attack, int health, String owner, Combat.CombatAbility... combatAbilities) {
		super(game, name, knowledge, CardType.Unit, text, owner);
		playable = new Playable(game, this, cost, () -> combat.setDeploying()).setSlow().setPersistent();
		studiable = new Studiable(game, this);
		activatable = new Activatable(game, this);
		triggering = new Triggering(game, this);
		combat = new Combat(game, this, attack, health);
		for (Combat.CombatAbility combatAbility : combatAbilities) {
			combat.addCombatAbility(combatAbility);
		}
	}

	public Unit (Game game, String name, int cost, CounterMap<Knowledge> knowledge, String text, int attack, int health, String owner, Arraylist<Combat.CombatAbility> combatAbilities) {
		this(game, name, cost, knowledge, text, attack, health, owner);
		combatAbilities.forEach(combatAbility -> combat.addCombatAbility(combatAbility));
	}


}
