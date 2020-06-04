package com.visitor.card;

import com.visitor.card.properties.*;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.helpers.containers.Damage;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Counter;
import com.visitor.protocol.Types.CounterGroup;
import com.visitor.protocol.Types.Knowledge;
import com.visitor.protocol.Types.KnowledgeGroup;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Supplier;

import static com.visitor.game.Game.Zone.Discard_Pile;
import static com.visitor.game.Game.Zone.Hand;
import static java.util.UUID.randomUUID;

/**
 * @author pseudo
 */
public abstract class Card implements Serializable {

	protected final Game game;
	public UUID id;
	public String name;
	public String text;
	public Arraylist<CardType> types;
	public Arraylist<CardSubtype> subtypes;


	public CounterMap<Knowledge> knowledge;

	public String owner;
	public String controller;

	public Activatable activatable;
	public Triggering triggering;
	public Combat combat;
	public Playable playable;
	public Studiable studiable;

	public boolean depleted;
	public CounterMap<Counter> counters;
	public Arraylist<UUID> targets;

	/**
	 * This is the default constructor for creating a card.
	 *
	 * @param name
	 * @param knowledge
	 * @param text
	 * @param owner
	 */
	public Card (Game game, String name,
	             CounterMap<Knowledge> knowledge,
	             CardType type, String text, String owner) {
		this.game = game;
		id = randomUUID();
		counters = new CounterMap<>();
		types = new Arraylist<>(type);
		subtypes = new Arraylist<>();
		targets = new Arraylist<>();
		this.name = name;
		this.knowledge = knowledge;
		this.text = text;
		this.owner = owner;
		this.controller = owner;
		this.depleted = false;
	}


	/**
	 * Function that adds counters to the card.
	 *
	 * @param name
	 * @param count
	 */
	public void addCounters (Counter name, int count) {
		counters.add(name, count);
	}

	public void removeCounters (Counter name, int count) {
		counters.decrease(name, count);
	}

	public int removeAllCounters (Counter name) {
		return counters.remove(name);
	}

	public void deplete () {
		depleted = true;
	}

	public void newTurn () {
		depleted = false;
		if (combat != null)
			combat.newTurn();
	}

	/**
	 * Function that clears status flags and supplementary data of the card.
	 */
	public void clear () {
		depleted = false;
		targets.clear();
		if (combat != null)
			combat.clear();
	}

	public void destroy () {
		clear();
		if (triggering != null) {
			triggering.deregister();
		}
		game.extractCard(id);
		game.putTo(controller, this, Discard_Pile);
	}

	public void sacrifice () {
		clear();
		if (triggering != null) {
			triggering.deregister();
		}
		game.extractCard(id);
		game.putTo(controller, this, Discard_Pile);
	}

	public void returnToHand () {
		clear();
		if (triggering != null) {
			triggering.deregister();
		}
		game.extractCard(id);
		game.putTo(controller, this, Hand);
	}

	public void copyPropertiesFrom (Card c) {
		id = c.id;
		owner = c.owner;
		controller = c.controller;
		counters = c.counters;
		depleted = c.depleted;
		targets = c.targets;
	}

	private final void runIfNotNull (Object object, Runnable runnable) {
		HelperFunctions.runIfNotNull(object, runnable, () ->
				System.out.println(toCardMessage().toString())
		);
	}

	private final <T> T runIfNotNull (Object object, Supplier<T> supplier) {
		return HelperFunctions.runIfNotNull(object, supplier, () -> {
			System.out.println(toCardMessage().toString());
			return null;
		});
	}

	public final void play () {
		runIfNotNull(playable, () -> playable.play());
	}

	public final void study (boolean normal) {
		runIfNotNull(studiable, () -> studiable.study(normal));
	}

	public final void checkEvent (Event e) {
		runIfNotNull(triggering, () -> triggering.checkEvent(e));
	}

	public final void resolve () {
		runIfNotNull(playable, () -> playable.resolve());
	}

	public final void receiveDamage (Damage damage, Card source) {
		runIfNotNull(combat, () -> combat.receiveDamage(damage, source));
	}

	public void heal (int healAmount) {
		runIfNotNull(combat, () -> combat.heal(healAmount));
	}

	public final int getAttack () {
		return runIfNotNull(combat, () -> combat.getAttack());
	}

	public void gainHealth (int health) {
		runIfNotNull(combat, () -> combat.gainHealth(health));
	}

	//TODO: Refactor these like above
	public final void unsetAttacking () {
		if (combat != null) {
			combat.unsetAttacking();
		} else {
			System.out.println("Trying to unset attacking a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public final void addBlocker (UUID blockerId) {
		if (combat != null) {
			combat.addBlocker(blockerId);
		} else {
			System.out.println("Trying to add blocker to a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public final void setBlocking (UUID blockedBy) {
		if (combat != null) {
			combat.setBlocking(blockedBy);
		} else {
			System.out.println("Trying to set blocking of a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public final void dealAttackDamage (boolean firstStrike) {
		if (combat != null) {
			combat.dealAttackDamage(firstStrike);
		} else {
			System.out.println("Trying to deal attack damage from a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public final void dealBlockDamage () {
		if (combat != null) {
			combat.dealBlockDamage();
		} else {
			System.out.println("Trying to deal block damage from a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public final void activate () {
		if (activatable != null) {
			activatable.activate();
		} else {
			System.out.println("Trying to activate a non-activatable card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public final void unsetBlocking () {
		if (combat != null) {
			combat.unsetBlocking();
		} else {
			System.out.println("Trying to unset blocking of a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public boolean canDieFromBlock () {
		if (combat != null) {
			return combat.canDieFromBlock();
		} else {
			System.out.println("Trying to check death of a non-combat card!");
			System.out.println(toCardMessage().toString());
			return false;
		}
	}

	public boolean canDieFromAttack () {
		if (combat != null) {
			return combat.canDieFromAttack();
		} else {
			System.out.println("Trying to check death of a non-combat card!");
			System.out.println(toCardMessage().toString());
			return false;
		}
	}

	public void maybeDieFromBlock () {
		if (canDieFromBlock() && combat != null) {
			game.destroy(id);
		} else {
			System.out.println("Trying to kill a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public void maybeDieFromAttack () {
		if (canDieFromAttack() && combat != null) {
			game.destroy(id);
		} else {
			System.out.println("Trying to kill a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public void addCombatAbility (Combat.CombatAbility ability) {
		if (combat != null) {
			combat.addCombatAbility(ability);
		} else {
			System.out.println("Trying to add combat ability to a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public void removeCombatAbility (Combat.CombatAbility ability) {
		if (combat != null) {
			combat.removeCombatAbility(ability);
		} else {
			System.out.println("Trying to remove combat ability to a non-combat card!");
			System.out.println(toCardMessage().toString());
		}
	}

	public final boolean canAttack () {
		return combat != null && combat.canAttack();
	}

	public final boolean canBlock () {
		return combat != null && combat.canBlock();
	}

	public final boolean canBlock (Card c) {
		return combat != null && combat.canBlock(c);
	}

	public final boolean canPlay () {
		return playable != null && playable.canPlay();
	}

	public final boolean canActivate () {
		return activatable != null && activatable.canActivate();
	}

	public final boolean canStudy () {
		return studiable != null && studiable.canStudy();
	}

	public final boolean isAttacking () {
		return combat != null && combat.isAttacking();
	}

	public final void setAttacking (UUID attacker) {
		runIfNotNull(combat, () -> combat.setAttacking(attacker));
	}

	public final boolean hasCombatAbility (Combat.CombatAbility combatAbility) {
		return combat != null && combat.hasCombatAbility(combatAbility);
	}

	public boolean hasType (CardType type) {
		return types.contains(type);
	}

	public boolean hasSubtype (CardSubtype type) {
		return subtypes.contains(type);
	}

	public Types.Card.Builder toCardMessage () {
		String packageName = this.getClass().getPackageName();
		String set = packageName.substring(packageName.lastIndexOf(".") + 1);
		Types.Card.Builder builder = Types.Card.newBuilder()
				.setId(id.toString())
				.setSet(set)
				.setName(name)
				.setDepleted(depleted)
				.setDescription(text)
				.setCombat(combat.toCombatMessage())
				.setLoyalty(-1)
				.addAllTypes(types.transformToStringList())
				.addAllSubtypes(subtypes.transformToStringList())
				.addAllTargets(targets.transformToStringList());

		counters.forEach((k, i) -> builder.addCounters(CounterGroup.newBuilder()
				.setCounter(k)
				.setCount(i).build()));

		knowledge.forEach((k, i) -> builder.addKnowledgeCost(KnowledgeGroup.newBuilder()
				.setKnowledge(k)
				.setCount(i).build()));

		if (playable != null)
			builder.setCost(playable.getCost() + "");

		return builder;
	}

	public UUID getId () {
		return id;
	}

	public void addTurnlyCombatAbility (Combat.CombatAbility combatAbility) {
		runIfNotNull(combat, () -> combat.addTurnlyCombatAbility(combatAbility));
	}

	public void endTurn () {
		if (combat != null)
			combat.endTurn();
	}

	public void addTurnlyAttack (int attack) {
		runIfNotNull(combat, () -> combat.addTurnlyAttack(attack));
	}

	public void addTurnlyHealth (int health) {
		runIfNotNull(combat, () -> combat.addTurnlyHealth(health));
	}

	public boolean hasColor (Knowledge knowledge) {
		return this.knowledge.contains(knowledge);
	}


	public enum CardType {
		Ally,
		Asset,
		Junk,
		Passive,
		Spell,
		Tome,
		Unit,
		Ability,
		Effect
	}

	public enum CardSubtype {
		Cantrip,
		Ritual,
	}

}