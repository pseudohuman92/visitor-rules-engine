package com.visitor.card.properties;

import com.visitor.game.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.containers.Damage;
import com.visitor.protocol.Types;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.visitor.card.properties.Combat.CombatAbility.*;

public class Combat {

	private final Card card;
	private final Game game;

	private int attack;
	private int health;
	private int maxHealth;
	private int shield;
	private boolean deploying;
	private UUID blockedAttacker;
	private Arraylist<UUID> blockedBy;
	private UUID attackTarget;
	private CounterMap<CombatAbility> combatAbilityList;

	private int turnlyAttack;
	private int turnlyHealth;
	private int turnlyShield;
	private CounterMap<CombatAbility> turnlyCombatAbilityList;

	private Supplier<Boolean> canAttackAdditional;
	private Supplier<Boolean> canAttack;
	private Predicate<Card> canBlockAdditional;
	private Predicate<Card> canBlock;
	private Supplier<Boolean> canBlockGeneral;
	private Consumer<UUID> setAttacking;
	private Runnable unsetAttacking;
	private Consumer<UUID> setBlocking;
	private Runnable unsetBlocking;
	private Consumer<UUID> addBlocker;
	private Runnable newTurn;
	private Consumer<Boolean> dealAttackDamage;
	private Runnable dealBlockDamage;
	private BiConsumer<Damage, Card> receiveDamage;
	private Arraylist<BiConsumer<UUID, Damage>> damageEffects;

	public Combat (Game game, Card card, int attack, int health) {
		this.card = card;
		this.game = game;
		this.attack = attack;
		this.health = health;
		this.maxHealth = health;
		blockedBy = new Arraylist<>();
		combatAbilityList = new CounterMap<>();
		turnlyCombatAbilityList = new CounterMap<>();
		damageEffects = new Arraylist<>();
		//Default implementations
		canAttackAdditional = () -> true;

		canAttack = () ->
				!card.isDepleted() &&
						(!deploying || hasCombatAbility(Haste)) &&
						!hasCombatAbility(Defender) &&
						canAttackAdditional.get();

		canBlockAdditional = (unit) -> true;

		canBlock = (unit) ->
				!card.isDepleted() &&
						!unit.hasCombatAbility(Unblockable) &&
						(!unit.hasCombatAbility(Flying) || hasCombatAbility(Flying) || hasCombatAbility(Reach)) &&
						canBlockAdditional.test(unit);

		canBlockGeneral = () -> !card.isDepleted();

		setAttacking = (target) -> {
			if (!hasCombatAbility(Vigilance))
				card.deplete();
			attackTarget = target;
		};

		unsetAttacking = () -> {
			attackTarget = null;
			blockedBy.clear();
		};

		setBlocking = (unit) -> blockedAttacker = unit;

		unsetBlocking = () -> blockedAttacker = null;

		addBlocker = (blocker) -> blockedBy.add(blocker);

		newTurn = () -> {
			if (hasCombatAbility(Regenerate)) {
				game.heal(card.id, combatAbilityList.get(Regenerate));
			}
			deploying = false;
		};

		dealAttackDamage = (firstStrike) -> {
			UUID id = card.id;
			if (blockedBy.isEmpty()) {
				game.dealDamage(id, attackTarget, new Damage(getAttack(), firstStrike, true));
			} else {
				if (blockedBy.size() == 1) {
					game.dealDamage(id, blockedBy.get(0), new Damage(getAttack(), firstStrike, true));
				} else {
					game.assignDamage(id, blockedBy, new Damage(getAttack(), firstStrike, true), hasCombatAbility(Trample));
				}
			}
		};

		dealBlockDamage = () -> {
			UUID id = card.id;
			if (blockedAttacker != null)
				game.dealDamage(id, blockedAttacker, new Damage(getAttack(), false, true));
		};

		receiveDamage = (damage, source) -> {
			int damageAmount = damage.amount;

			//Apply shields
			if (turnlyShield >= damageAmount) {
				turnlyShield -= damageAmount;
				return;
			}
			damageAmount -= turnlyShield;
			turnlyShield = 0;

			if (shield >= damageAmount) {
				shield -= damageAmount;
				return;
			}
			damageAmount -= shield;
			shield = 0;

			int dealtDamageAmount = damageAmount;

			//Trample Damage
			// TODO: Figure out assigning more than health to make trample go through
			// while there are other blockers to assign damage
			if (source.isAttacking() &&
					source.hasCombatAbility(Trample) &&
					damageAmount > getHealth()) {

				int leftoverDamage = damageAmount - getHealth();
				this.turnlyHealth = 0;
				this.health = 0;
				try {
					game.dealDamage(source.id, card.controller, new Damage(leftoverDamage));
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}

			} else { //Normal damage
				if (turnlyHealth >= damageAmount) {
					turnlyHealth -= damageAmount;
				} else {
					damageAmount -= turnlyHealth;
					turnlyHealth = 0;
					this.health -= Math.min(damageAmount, this.health);
				}
			}

			if (card != null && damage.mayKill && (getHealth() == 0 || source.hasCombatAbility(Deathtouch))) {
				game.destroy(source.id, card.id);
			}

			if (source.hasCombatAbility(Lifelink)) {
				game.gainHealth(source.controller, dealtDamageAmount);
			}
			if (source.hasCombatAbility(Drain)) {
				game.heal(source.id, dealtDamageAmount);
			}
		};
	}


	public Combat (Game game, Card card, int health) {
		this(game, card, -1, health);
		canAttack = () -> false;
		canBlockGeneral = () -> false;
	}


	public final boolean canAttack () {
		return canAttack.get();
	}

	public final boolean canBlock () {
		return canBlockGeneral.get();
	}

	public final boolean canBlock (Card unit) {
		return canBlock.test(unit);
	}

	public final void unsetAttacking () {
		unsetAttacking.run();
	}

	public final void setBlocking (UUID unit) {
		setBlocking.accept(unit);
	}

	public final void unsetBlocking () {
		unsetBlocking.run();
	}

	public final void addBlocker (UUID blocker) {
		addBlocker.accept(blocker);
	}

	public final void newTurn () {
		newTurn.run();
	}

	public final void dealAttackDamage (boolean firstStrike) {
		dealAttackDamage.accept(firstStrike);
	}

	public final void dealBlockDamage () {
		dealBlockDamage.run();
	}

	public final void receiveDamage (Damage damage, Card source) {
		receiveDamage.accept(damage, source);
	}

	public final void clearTurnly () {
		turnlyAttack = 0;
		turnlyHealth = 0;
		turnlyShield = 0;
		turnlyCombatAbilityList.clear();
	}

	public final void clearCombatTargetData () {
		attackTarget = null;
		blockedAttacker = null;
		blockedBy.clear();
	}

	public final void clear () {
		clearTurnly();
		clearCombatTargetData();
		deploying = false;
	}

	public final void endTurn () {
		clearTurnly();
		clearCombatTargetData();
	}

	public Types.Combat.Builder toCombatMessage () {
		return Types.Combat.newBuilder()
				.setAttack(getAttack())
				.setAttackTarget(attackTarget != null ? attackTarget.toString() : "")
				.setBlockedAttacker(blockedAttacker != null ? blockedAttacker.toString() : "")
				.setDeploying(!hasCombatAbility(Haste) && deploying)
				.setHealth(getHealth())
				.setShield(getShield())
				.addAllCombatAbilities(combatAbilityList.transformToStringList())
				.addAllCombatAbilities(turnlyCombatAbilityList.transformToStringList());
	}

	public void addCombatAbility (CombatAbility combatAbility, int i) {
		combatAbilityList.add(combatAbility, i);
	}

	public final void addCombatAbility (CombatAbility combatAbility) {
		addCombatAbility(combatAbility, 1);
	}

	public void addTurnlyCombatAbility (CombatAbility combatAbility, int i) {
		turnlyCombatAbilityList.add(combatAbility, i);
	}

	public final void addTurnlyCombatAbility (CombatAbility combatAbility) {
		addTurnlyCombatAbility(combatAbility, 1);
	}

	public void removeCombatAbility (CombatAbility combatAbility, int i) {
		combatAbilityList.decrease(combatAbility, i);
	}

	public void removeCombatAbility (CombatAbility combatAbility) {
		removeCombatAbility(combatAbility, 1);
	}

	public void addDamageEffect (BiConsumer<UUID, Damage> effect) {
		damageEffects.add(effect);
	}

	public final boolean hasCombatAbility (CombatAbility combatAbility) {
		return combatAbilityList.contains(combatAbility) || turnlyCombatAbilityList.contains(combatAbility);
	}

	public int getAttack () {
		return attack + turnlyAttack;
	}

	public int getShield () {
		return shield + turnlyShield;
	}

	public int getHealth () {
		return health + turnlyHealth;
	}


	public final void heal (int health) {
		if (this.health < maxHealth) {
			this.health = Math.min(maxHealth, this.health + health);
		}
	}

	//Do not use to lose health (e.g. negative input)
	//Also raises the max health
	public final void addAttackAndHealth (int attack, int health) {
		if (attack > 0)
			this.attack += attack;

		if (health > 0) {
			this.health += health;
			this.maxHealth += health;
		}
	}

	public boolean canDieFromBlock () {
		return getHealth() == 0 || blockedBy.hasOne(blockerId -> game.getCard(blockerId).hasCombatAbility(Deathtouch));
	}

	public boolean canDieFromAttack () {
		return getHealth() == 0 || game.getCard(blockedAttacker).hasCombatAbility(Deathtouch);
	}

	public boolean isAttacking () {
		return attackTarget != null;
	}

	public final void setAttacking (UUID target) {
		setAttacking.accept(target);
	}

	public void setDeploying () {
		deploying = true;
	}

	public void loseHealth (int amount) {
		if (turnlyHealth >= amount) {
			turnlyHealth -= amount;
		} else {
			amount -= turnlyHealth;
			turnlyHealth = 0;
			this.health -= Math.min(amount, this.health);
		}
	}

	public void addTurnlyAttackAndHealth (int attack, int health) {
		if (attack > 0)
			turnlyAttack += attack;
		if (health > 0)
			turnlyHealth += health;
	}

	public void setAttack (int i) {
		attack = i;
	}

	//Also sets the max health
	public void setHealth (int i) {
		health = i;
		maxHealth = i;
	}

	public boolean isDeploying () {
		return deploying;
	}

	public void addShield (int i) {
		shield += i;
	}

	public void triggerDamageEffects (UUID targetId, Damage damage) {
		damageEffects.forEach(effect -> effect.accept(targetId, damage));
	}

	public void loseAttack (int i) {
		attack = Math.max(attack - i, 0);
	}

	public enum CombatAbility {
		Deathtouch, //Done
		Defender, //Done
		FirstStrike, //Done
		Trample, //Done
		Flying, //Done
		Haste, //Done
		Vigilance, //Done
		Lifelink, //Done
		Reach, //Done
		Drain, //Done
		Unblockable, //Done

		//Numbered abilities
		Regenerate //Done
	}

}
