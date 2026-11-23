package com.visitor.card.properties;

import com.visitor.card.Card;
import com.visitor.game.Event;
import com.visitor.game.parts.Base;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.card.containers.Damage;
import com.visitor.protocol.Types;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.visitor.card.properties.Damagable.CombatAbility.*;

public class Damagable {

    private final Card card;
    private final Game game;

    private UUID playerId;

    private int attack;
    private int turnlyAttack;
    private int health;
    private int turnlyHealth;
    private int maxHealth;
    private int shield;
    private int turnlyShield;

    private boolean deploying;
    private boolean receivedDeadlyCombatDamage;
    private UUID blockedAttacker;
    private UUID blockedBy;
    private UUID attackTarget;
    private final CounterMap<CombatAbility> combatAbilityList;
    private final CounterMap<CombatAbility> turnlyCombatAbilityList;

    private final Arraylist<Supplier<Boolean>> canAttackAdditional;
    private final Arraylist<Predicate<Card>> canBlockAdditional;
;

    public Damagable(Game game, Card card, int attack, int health, int shield) {
        this.card = card;
        this.game = game;
        this.attack = attack;
        this.health = health;
        this.shield = shield;
        this.maxHealth = health;
        this.playerId = null;
        combatAbilityList = new CounterMap<>();
        turnlyCombatAbilityList = new CounterMap<>();
        canAttackAdditional = new Arraylist<>();
        canBlockAdditional = new Arraylist<>();
    }


    public void loseMaxHealth(int i) {
        maxHealth = Math.max(maxHealth - i, 0);
    }


    public Damagable(Game game, Card card, int health) {
        this(game, card, -1, health, 0);
        canAttackAdditional.add(() -> false);
        canBlockAdditional.add(u -> false);
    }


    public final boolean canAttack() {
        boolean b =  game.canAttack(card.controller) &&
                !card.isDepleted() &&
                (!deploying || hasCombatAbility(CombatAbility.Blitz)) &&
                !hasCombatAbility(Defender) &&
                card.zone == Base.Zone.Play;
        for (Supplier<Boolean> s : canAttackAdditional){
            b = b && s.get();
        }
        return b;
    }

    public final boolean canBlock() {
        return game.canBlock(card.controller) && !card.isDepleted() && card.getZone() == Base.Zone.Play;
    }

    public final boolean canBlock(Card unit) {
        boolean b = !card.isDepleted() &&
                !unit.hasCombatAbility(Unblockable) &&
                (!unit.hasCombatAbility(Evasive) || hasCombatAbility(Evasive) || hasCombatAbility(Reach));
        for (Predicate<Card> p : canBlockAdditional){
            b = b && p.test(unit);
        }
        return b;
    }

    public final void unsetAttacking() {
        attackTarget = null;
        blockedBy = null;
        receivedDeadlyCombatDamage = false;
    }

    public final void setBlocking(UUID unit) {
        card.deplete();
        blockedAttacker = unit;
    }

    public final void unsetBlocking() {
        blockedAttacker = null;
        receivedDeadlyCombatDamage = false;
    }

    public final void addBlocker(UUID blocker) {
        blockedBy = blocker;
    }

    public final void newTurn() {
        if (hasCombatAbility(Regenerate)) {
            game.heal(card.getId(), combatAbilityList.get(Regenerate));
        }
        if (hasCombatAbility(Decay)) {
            loseMaxHealth(1);
            loseHealth(1);
            loseAttack(1);
        }
        deploying = false;
    }

    public final void dealAttackDamage() {
        UUID id = card.getId();
        if (blockedBy == null) {
            game.dealDamage(id, attackTarget, new Damage(getAttack(), false, true));
        } else {
            Card blocker = game.getCard(blockedBy);
            if (blocker != null && blocker.zone == Base.Zone.Play) {
                game.dealDamage(id, blockedBy, new Damage(getAttack(), false, true));
            } else if (hasCombatAbility(Trample)) {
                game.dealDamage(id, attackTarget, new Damage(getAttack(), false, true));
            }
        }
    }

    public final void dealBlockDamage() {
        UUID id = card.getId();
        Card attacker = game.getCard(blockedAttacker);
        if (blockedAttacker != null && attacker != null && attacker.zone == Base.Zone.Play)
            game.dealDamage(id, blockedAttacker, new Damage(getAttack(), false, true));
    }

    public final void receiveDamage(Damage damage, Card source) {
        int damageAmount = damage.amount - (shield + turnlyShield);

        //Apply shields
        if (damageAmount <= 0) {
            return;
        }

        int dealtDamageAmount = Math.min(damageAmount, getHealth());

        if (source != null && source.isAttacking() &&
                source.hasCombatAbility(Trample) &&
                damageAmount > getHealth()) {

            int leftoverDamage = damageAmount - getHealth();
            this.turnlyHealth = 0;
            this.health = 0;
            try {
                game.dealDamage(source.getId(), card.controller, new Damage(leftoverDamage));
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

        if (source != null && source.hasCombatAbility(Lifelink)) {
            game.gainHealth(source.controller, dealtDamageAmount);
        }
        if (source != null && source.hasCombatAbility(Drain)) {
            game.heal(source.getId(), dealtDamageAmount);
        }



        if (damage.combat && source != null && source.hasCombatAbility(Deadly) && dealtDamageAmount > 0) {
            receivedDeadlyCombatDamage = true;
        }
        if (card != null && damage.mayKill && dealtDamageAmount > 0
                && (getHealth() == 0 || (source != null && source.hasCombatAbility(Deadly)))) {
            game.destroy(card.getId());
        }

        game.addEvent(Event.damage(source, playerId != null ? playerId : card.getId(), new Damage(dealtDamageAmount, damage.mayKill, damage.combat)));
    }

    public final void clearTurnly() {
        turnlyAttack = 0;
        turnlyHealth = 0;
        turnlyShield = 0;
        turnlyCombatAbilityList.clear();
    }

    public final void clearCombatTargetData() {
        attackTarget = null;
        blockedAttacker = null;
        blockedBy = null;
        receivedDeadlyCombatDamage = false;
    }

    public final void clear() {
        clearTurnly();
        clearCombatTargetData();
        deploying = false;
    }

    public final void endTurn() {
        clearTurnly();
        clearCombatTargetData();
    }

    public Types.Combat.Builder toCombatMessage() {
        return Types.Combat.newBuilder()
                .setAttack(getAttack())
                .setAttackTarget(attackTarget != null ? attackTarget.toString() : "")
                .setBlockedAttacker(blockedAttacker != null ? blockedAttacker.toString() : "")
                .setDeploying(!hasCombatAbility(CombatAbility.Blitz) && deploying)
                .setHealth(getHealth())
                .setShield(getShield())
                .addAllCombatAbilities(combatAbilityList.transformToStringList())
                .addAllCombatAbilities(turnlyCombatAbilityList.transformToStringList())
                .addAllPossibleBlockTargets(new Arraylist<String>((List<String>) game.getPossibleBlockTargets(this::canBlock).transform(c -> c.getId().toString())));
    }

    public void addCombatAbility(CombatAbility combatAbility, int i, boolean turnly) {
        if (turnly)
            turnlyCombatAbilityList.add(combatAbility, i);
        else
            combatAbilityList.add(combatAbility, i);
    }

    public final void addCombatAbility(CombatAbility combatAbility, boolean turnly) {
        addCombatAbility(combatAbility, 1, turnly);
    }

    public void removeCombatAbility(CombatAbility combatAbility, int i) {
        turnlyCombatAbilityList.decrease(combatAbility, i);
        combatAbilityList.decrease(combatAbility, i);
    }

    public void removeCombatAbility(CombatAbility combatAbility) {
        removeCombatAbility(combatAbility, 1);
    }

    public final boolean hasCombatAbility(CombatAbility combatAbility) {
        return combatAbilityList.contains(combatAbility) || turnlyCombatAbilityList.contains(combatAbility);
    }

    public int getAttack() {
        return attack + turnlyAttack;
    }

    public int getShield() {
        return shield + turnlyShield;
    }

    public int getHealth() {
        return health + turnlyHealth;
    }


    public final void heal(int health) {
        if (this.health < maxHealth) {
            this.health = Math.min(maxHealth, this.health + health);
        }
    }

    //Do not use to lose health (e.g. negative input)
    //Also raises the max health
    public final void addAttackAndHealth(int attack, int health, boolean turnly) {
        if (turnly) {
            if (attack > 0)
                this.turnlyAttack += attack;

            if (health > 0) {
                this.turnlyHealth += health;
            }
        } else {
            if (attack > 0)
                this.attack += attack;

            if (health > 0) {
                this.health += health;
                this.maxHealth += health;
            }
        }
    }

    public boolean canDieFromBlock() {
        return getHealth() == 0 || receivedDeadlyCombatDamage;
    }

    public boolean canDieFromAttack() {
        return getHealth() == 0 || receivedDeadlyCombatDamage;
    }

    public boolean isAttacking() {
        return attackTarget != null;
    }

    public final void setAttacking(UUID target) {
        if (!hasCombatAbility(Vigilance))
            card.deplete();
        attackTarget = target;
    }

    public void setDeploying() {
        deploying = true;
    }

    public void loseHealth(int amount) {
        if (turnlyHealth >= amount) {
            turnlyHealth -= amount;
        } else {
            amount -= turnlyHealth;
            turnlyHealth = 0;
            this.health -= Math.min(amount, this.health);
            if (health <= 0 && card != null) {
                game.destroy(card.getId());
            }
        }
    }

    public void setAttack(int i) {
        attack = i;
    }

    //Also sets the max health
    public void setHealth(int i) {
        health = i;
        maxHealth = i;
    }

    public boolean isDeploying() {
        return deploying;
    }

    public void addShield(int i, boolean turnly) {
        if (turnly) {
            turnlyShield += i;
        } else {
            shield += i;
        }
    }



    public void loseAttack(int i) {
        attack = Math.max(attack - i, 0);
    }

    public int drain(int amount) {
        if (amount <= 0) return 0;
        int drained = Math.min(turnlyHealth, amount);
        turnlyHealth -= drained;
        int remaining = amount - drained;
        if (remaining > 0) {
            int fromHealth = Math.min(health, remaining);
            drained += fromHealth;
            health -= fromHealth;
            if (health <= 0 && card != null) {
                game.destroy(card.getId());
            }
        }
        return drained;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void removeShield(int shieldAmount) {
        if (turnlyShield >= shieldAmount){
            turnlyShield -= shieldAmount;
        } else {
            shieldAmount -= turnlyShield;
            turnlyShield = 0;
            shield = Math.max(shield - shieldAmount, 0);
        }
    }

    public CounterMap<CombatAbility> getCombatAbilities() {
        CounterMap<CombatAbility> a = new CounterMap<>();
        a.merge(turnlyCombatAbilityList);
        a.merge(combatAbilityList);
        return a;
    }

    public void setPlayerId(UUID id) {
        playerId = id;
    }

    public enum CombatAbility {
        Deadly, //Done
        Defender, //Done
        FirstStrike, //Done
        Trample, //Done
        Evasive, //Done
        Vigilance, //Done
        Lifelink, //Done
        Reach, //Done
        Drain, //Done
        Unblockable, //Done
        Blitz,
        Decay,

        //Numbered abilities
        Regenerate //Done
    }

}
