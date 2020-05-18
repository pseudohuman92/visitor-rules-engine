package com.visitor.card.types;

import com.visitor.card.properties.*;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Counter;
import com.visitor.protocol.Types.CounterGroup;
import com.visitor.protocol.Types.Knowledge;
import com.visitor.protocol.Types.KnowledgeGroup;

import java.io.Serializable;
import java.util.UUID;

import static com.visitor.game.Game.Zone.Hand;
import static com.visitor.game.Game.Zone.Scrapyard;
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
    public Arraylist<String> subtypes;


    public Hashmap<Types.Knowledge, Integer> knowledge;

    public String owner;
    public String controller;

    public Activatable activatable;
    public Triggering triggering;
    public Combat combat;
    public Playable playable;
    public Studiable studiable;
    //public Targeting targeting;

    public boolean depleted;
    public Hashmap<Counter, Integer> counters;
    public Arraylist<UUID> targets;

    /**
     * This is the default constructor for creating a card.
     *
     * @param name
     * @param knowledge
     * @param text
     * @param owner
     */
    public Card(Game g, String name,
                Hashmap<Knowledge, Integer> knowledge,
                CardType type, String text, String owner) {
        game = g;
        id = randomUUID();
        counters = new Hashmap<>();
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
    public void addCounters(Counter name, int count) {
        counters.merge(name, count, (a, b) -> a + b);
    }

    public boolean removeCounters(Counter name, int count) {
        int k = counters.get(name);
        if (k <= count) {
            counters.remove(name);
            return false;
        } else {
            counters.put(name, k - count);
            return true;
        }
    }

    public int removeAllCounters(Counter name) {
        return counters.remove(name);
    }

    public void deplete() {
        depleted = true;
    }

    public void ready() {
        depleted = false;
        if (combat != null)
            combat.ready();
    }

    /**
     * Function that clears status flags and supplementary data of the card.
     */
    public void clear() {
        depleted = false;
        targets.clear();
        if (combat != null)
            combat.clear();
    }

    public void destroy() {
        clear();
        if (triggering != null) {
            game.removeTriggeringCard(this);
        }
        game.extractCard(id);
        game.putTo(controller, this, Scrapyard);
    }

    public void sacrifice() {
        clear();
        if (triggering != null) {
            game.removeTriggeringCard(this);
        }
        game.extractCard(id);
        game.putTo(controller, this, Scrapyard);
    }

    public void returnToHand() {
        clear();
        if (triggering != null) {
            game.removeTriggeringCard(this);
        }
        game.extractCard(id);
        game.putTo(controller, this, Hand);
    }

    public void copyPropertiesFrom(Card c) {
        id = c.id;
        owner = c.owner;
        controller = c.controller;
        counters = c.counters;
        depleted = c.depleted;
        targets = c.targets;
    }


    public Types.Card.Builder toCardMessage() {
        Types.Card.Builder builder = Types.Card.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setDepleted(depleted)
                .setDescription(text)
                .setCombat(combat.toCombatMessage())
                .addAllTypes(types.transformToStringList())
                .addAllSubtypes(subtypes)
                .addAllTargets(targets.transformToStringList());

        counters.forEach((k, i) -> builder.addCounters(CounterGroup.newBuilder()
                .setCounter(k)
                .setCount(i).build()));

        knowledge.forEach((k, i) -> builder.addKnowledgeCost(KnowledgeGroup.newBuilder()
                .setKnowledge(k)
                .setCount(i).build()));

        if (playable != null)
            builder.setCost(playable.getCost()+"");

        return builder;
    }

    public final void play() {
        if (playable != null) {
            playable.play();
        } else {
            System.out.println("Trying to play a non-playable card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void study(boolean normal) {
        if (studiable != null) {
            studiable.study(normal);
        } else {
            System.out.println("Trying to study a non-studiable card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void checkEvent(Event e) {
        if (triggering != null) {
            triggering.checkEvent(e);
        } else {
            System.out.println("Trying to check an event by non-triggering card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void resolve() {
        if (playable != null) {
            playable.resolve();
        } else {
            System.out.println("Trying to resolve a non-playable card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final boolean canAttack() {
        if (combat != null) {
            return combat.canAttack();
        } else {
            System.out.println("Trying to determine canAttack a non-combat card!");
            System.out.println(toCardMessage().toString());
            return false;
        }
    }

    public final boolean canBlock() {
        if (combat != null) {
            return combat.canBlock();
        } else {
            System.out.println("Trying to determine canBlock a non-combat card!");
            System.out.println(toCardMessage().toString());
            return false;
        }
    }

    public final boolean canBlock(Card c) {
        if (combat != null) {
            return combat.canBlock(c);
        } else {
            System.out.println("Trying to determine canBlock a non-combat card!");
            System.out.println(toCardMessage().toString());
            return false;
        }
    }

    public final boolean canPlay() {
        if (playable != null) {
            return playable.canPlay();
        } else {
            System.out.println("Trying to determine canPlay a non-playable card!");
            System.out.println(toCardMessage().toString());
            return false;
        }
    }

    public final void dealDamage(int damage, Card source) {
        if (combat != null) {
            combat.receiveDamage(damage,  source);
        } else {
            System.out.println("Trying to deal damage to a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final boolean canActivate() {
        if (activatable != null) {
            return activatable.canActivate();
        } else {
            System.out.println("Trying to determine canActivate to a non-activatable card!");
            System.out.println(toCardMessage().toString());
            return false;
        }
    }

    public final void setAttacking(UUID attacker) {
        if (combat != null) {
            combat.setAttacking(attacker);
        } else {
            System.out.println("Trying to set attacking a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void unsetAttacking() {
        if (combat != null) {
            combat.unsetAttacking();
        } else {
            System.out.println("Trying to unset attacking a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final boolean canStudy() {
        if (studiable != null) {
            return studiable.canStudy();
        } else {
            System.out.println("Trying to determine canStudy a non-studiable card!");
            System.out.println(toCardMessage().toString());
            return false;
        }
    }

    public final void addBlocker(UUID blockerId) {
        if (combat != null) {
            combat.addBlocker(blockerId);
        } else {
            System.out.println("Trying to add blocker to a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void setBlocking(UUID blockedBy) {
        if (combat != null) {
            combat.setBlocking(blockedBy);
        } else {
            System.out.println("Trying to set blocking of a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void dealAttackDamage() {
        if (combat != null) {
            combat.dealAttackDamage();
        } else {
            System.out.println("Trying to deal attack damage from a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void dealBlockDamage() {
        if (combat != null) {
            combat.dealBlockDamage();
        } else {
            System.out.println("Trying to deal block damage from a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void activate() {
        if (activatable != null) {
            activatable.activate();
        } else {
            System.out.println("Trying to activate a non-activatable card!");
            System.out.println(toCardMessage().toString());
        }
    }

    public final void unsetBlocking() {
        if (combat != null) {
            combat.unsetBlocking();
        } else {
            System.out.println("Trying to unset blocking of a non-combat card!");
            System.out.println(toCardMessage().toString());
        }
    }


    public enum CardType {
        Ally,
        Asset,
        Junk,
        Passive,
        Ritual,
        Spell,
        Tome,
        Unit,
        Ability,
        Effect
    }

}