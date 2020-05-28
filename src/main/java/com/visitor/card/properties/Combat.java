package com.visitor.card.properties;

import com.visitor.card.Card;
import com.visitor.helpers.containers.Damage;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
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

    public int attack;
    public int maxHealth;
    public int health;
    public int shield;
    public boolean deploying;
    public UUID blockedAttacker;
    public Arraylist<UUID> blockedBy;
    public UUID attackTarget;
    public CounterMap<CombatAbility> combatAbilityList;

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
    private Runnable ready;
    private Consumer<Boolean> dealAttackDamage;
    private Runnable dealBlockDamage;
    private BiConsumer<Damage, Card> receiveDamage;

    public Combat(Game game, Card card, int attack, int health) {
        this.card = card;
        this.game = game;
        this.attack = attack;
        this.health = health;
        this.maxHealth = health;
        blockedBy = new Arraylist<>();
        combatAbilityList = new CounterMap<>();

        //Default implementations
        canAttackAdditional = () -> true;

        canAttack = () ->
                !card.depleted &&
                (!deploying || hasCombatAbility(Haste)) &&
                !hasCombatAbility(Defender) &&
                canAttackAdditional.get();

        canBlockAdditional = (unit) -> true;

        canBlock = (unit) ->
                !card.depleted &&
                !unit.hasCombatAbility(Unblockable) &&
                (!unit.hasCombatAbility(Flying) || hasCombatAbility(Flying) || hasCombatAbility(Reach)) &&
                canBlockAdditional.test(unit);

        canBlockGeneral = () -> !card.depleted;

        setAttacking = (target) -> {
            if (!hasCombatAbility(Vigilance))
                card.depleted = true;
            attackTarget = target;
        };

        unsetAttacking = () -> {
            attackTarget = null;
            blockedBy.clear();
        };

        setBlocking = (unit) -> blockedAttacker = unit;

        unsetBlocking = () -> blockedAttacker = null;

        addBlocker = (blocker) -> blockedBy.add(blocker);

        ready = () -> {
            if (hasCombatAbility(Regenerate)){
                game.gainHealth(card.id, combatAbilityList.get(Regenerate));
            }
            deploying = false;
        };

        dealAttackDamage = (firstStrike) -> {
            UUID id = card.id;
            if (blockedBy.isEmpty()) {
                game.dealDamage(id, attackTarget, new Damage(attack, firstStrike));
            } else {
                if (blockedBy.size() == 1) {
                    game.dealDamage(id, blockedBy.get(0), new Damage(attack, firstStrike));
                } else {
                    game.assignDamage(id, blockedBy, new Damage(attack, firstStrike));
                }
            }
        };

        dealBlockDamage = () -> {
            UUID id = card.id;
            if (blockedAttacker != null)
                game.dealDamage(id, blockedAttacker, new Damage(attack, false));
        };

        receiveDamage = (damage, source) -> {
            UUID id = card.id;
            int damageAmount = damage.amount;

            //Apply shields
            if (shield >= damageAmount) {
                shield -= damageAmount;
                return;
            }
            damageAmount -= shield;
            shield = 0;

            //Trample Damage
            //TODO: Figure out assigning more than health to make trample go through
            // while there are other blockers to assign damage
            if (source.isAttacking() &&
                source.hasCombatAbility(Trample) &&
                damageAmount > this.health) {

                int leftoverDamage = damageAmount - this.health;
                this.health = 0;
                game.dealDamage(source.id, game.getUserId(card.controller), new Damage(leftoverDamage));

            } else { //Normal damage
                this.health -= Math.min(damageAmount, this.health);
            }

            if (damage.mayKill && (this.health == 0 || source.hasCombatAbility(Deathtouch))) {
                game.destroy(source.id, id);
            }

            if (source.hasCombatAbility(Lifelink)) {
                game.gainHealth(source.controller, damageAmount);
            }
            if (source.hasCombatAbility(Drain)) {
                game.gainHealth(source.id, damageAmount);
            }
        };
    }

    public Combat(Game game, Card card, int health) {
        this(game, card, -1, health);
        canAttackAdditional = () -> false;
    }

    public final boolean canAttack() {
        return canAttack.get();
    }

    public final boolean canBlock() {
        return canBlockGeneral.get();
    }

    public final boolean canBlock(Card unit) {
        return canBlock.test(unit);
    }

    public final void setAttacking(UUID target) {
        setAttacking.accept(target);
    }

    public final void unsetAttacking() {
        unsetAttacking.run();
    }

    public final void setBlocking(UUID unit) {
        setBlocking.accept(unit);
    }

    public final void unsetBlocking() {
        unsetBlocking.run();
    }

    public final void addBlocker(UUID blocker) {
        addBlocker.accept(blocker);
    }

    public final void ready() {
        ready.run();
    }

    public final void dealAttackDamage(boolean firstStrike) {
        dealAttackDamage.accept(firstStrike);
    }

    public final void dealBlockDamage() {
        dealBlockDamage.run();
    }

    public final void receiveDamage(Damage damage, Card source) {
        receiveDamage.accept(damage, source);
    }

    public final void resetShields() {
        shield = 0;
    }

    public final void clear() {
        shield = 0;
        deploying = false;
        attackTarget = null;
        blockedAttacker = null;
        blockedBy.clear();
    }

    public Types.Combat.Builder toCombatMessage() {
        return Types.Combat.newBuilder()
                .setAttack(attack)
                .setAttackTarget(attackTarget != null ? attackTarget.toString() : "")
                .setBlockedAttacker(blockedAttacker != null ? blockedAttacker.toString() : "")
                .setDeploying(!hasCombatAbility(Haste) && deploying)
                .setHealth(health)
                .setShield(shield)
                .addAllCombatAbilities(new Arraylist<>(combatAbilityList.keySet()).transformToStringList());
    }

    public void addCombatAbility(CombatAbility combatAbility, int i) {
        combatAbilityList.add(combatAbility, i);
    }

    public final void addCombatAbility(CombatAbility combatAbility) {
        addCombatAbility(combatAbility, 1);
    }

    public final boolean hasCombatAbility(CombatAbility combatAbility) {
        return combatAbilityList.contains(combatAbility);
    }

    public final void gainHealth(int health) {
        this.health = Math.min(maxHealth, this.health+health);
    }

    public void removeCombatAbility(CombatAbility combatAbility, int i) {
        combatAbilityList.decrease(combatAbility, i);
    }

    public void removeCombatAbility(CombatAbility combatAbility) {
        removeCombatAbility(combatAbility, 1);
    }

    public boolean canDieFromBlock() {
        return health == 0 || blockedBy.hasOne(blockerId -> game.getCard(blockerId).hasCombatAbility(Deathtouch));
    }

    public boolean canDieFromAttack() {
        return health == 0 || game.getCard(blockedAttacker).hasCombatAbility(Deathtouch);
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
