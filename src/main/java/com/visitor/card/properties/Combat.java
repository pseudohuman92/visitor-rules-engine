package com.visitor.card.properties;

import com.visitor.card.types.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
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
    public int health;
    public int shield;
    public boolean deploying;
    public UUID blockedAttacker;
    public Arraylist<UUID> blockedBy;
    public UUID attackTarget;
    public Arraylist<CombatAbility> combatAbilittList;

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
    private Runnable dealAttackDamage;
    private Runnable dealBlockDamage;
    private BiConsumer<Integer, Card> receiveDamage;

    public Combat(Game game, Card card, int attack, int health) {
        this.card = card;
        this.game = game;
        this.attack = attack;
        this.health = health;
        blockedBy = new Arraylist<>();
        combatAbilittList = new Arraylist<>();

        //Default implementations
        canAttackAdditional = () -> true;
        canAttack = () -> !card.depleted && (!deploying || combatAbilittList.contains(Haste)) && !combatAbilittList.contains(Defender) && canAttackAdditional.get();
        canBlockAdditional = (unit) -> true;
        canBlock = (unit) -> !card.depleted && (!unit.combat.combatAbilittList.contains(Flying) || combatAbilittList.contains(Flying)) && canBlockAdditional.test(unit);
        canBlockGeneral = () -> !card.depleted;
        setAttacking = (target) -> {
            if (!combatAbilittList.contains(Vigilance))
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
        ready = () -> deploying = false;
        dealAttackDamage = () -> {
            UUID id = card.id;
            if (blockedBy.isEmpty()) {
                game.dealDamage(id, attackTarget, attack);
            } else {
                if (blockedBy.size() == 1) {
                    game.dealDamage(id, blockedBy.get(0), attack);
                } else {
                    game.assignDamage(id, blockedBy, attack);
                }
            }
        };
        dealBlockDamage = () -> {
            UUID id = card.id;
            if (blockedAttacker != null)
                game.dealDamage(id, blockedAttacker, attack);
        };
        receiveDamage = (damageAmount, source) -> {
            UUID id = card.id;
            int damage = damageAmount;

            //Apply shields
            if (shield >= damage) {
                shield -= damage;
                return;
            }
            damage -= shield;
            shield = 0;

            //Trample Damage
            if (damage > this.health && source.combat.combatAbilittList.contains(Trample)) {
                this.health = 0;
                game.dealDamage(id, game.getUserId(card.controller), damage - this.health);
                game.destroy(source.id, id);

            } else { //Normal damage
                this.health -= Math.min(damage, this.health);
                if (this.health == 0 || combatAbilittList.contains(Deathtouch)) {
                    game.destroy(source.id, id);
                }
            }
            if (source.combat.combatAbilittList.contains(Lifelink)){
                game.gainHealth(source.controller, damage);
            }
        };
    }

    public Combat(Game game, Card card, int health) {
        this(game, card, 0, health);
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

    public final void dealAttackDamage() {
        dealAttackDamage.run();
    }

    public final void dealBlockDamage() {
        dealBlockDamage.run();
    }

    public final void receiveDamage(int damage, Card source) {
        receiveDamage.accept(damage, source);
    }

    public final void resetShields() {
        shield = 0;
    }

    public final void clear() {
        shield = 0;
        deploying = false;
    }

    public Types.Combat.Builder toCombatMessage() {
        return Types.Combat.newBuilder()
                .setAttack(attack)
                .setAttackTarget(attackTarget.toString())
                .setBlockedAttacker(blockedAttacker.toString())
                .setDeploying(deploying)
                .setHealth(health)
                .setShield(shield)
                .addAllCombatAbilities(combatAbilittList.transformToStringList());
    }

    public void addCombatAbility(CombatAbility combatAbility) {
        combatAbilittList.add(combatAbility);
    }

    public enum CombatAbility {
        Deathtouch, //Done
        Defender, //Done
        FirstStrike,
        Trample, //Done
        Flying, //Done
        Haste, //Done
        Vigilance, //Done
        Lifelink //Done
    }
}
