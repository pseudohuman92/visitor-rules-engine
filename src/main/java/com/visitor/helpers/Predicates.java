/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.helpers;

import com.visitor.card.Card;
import com.visitor.game.Player;

import static com.visitor.card.Card.CardType.*;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public abstract class Predicates {

    public static boolean isAsset(Card card) {
        return card.hasType(Asset);
    }

    public static boolean isSpell(Card card) {
        return card.hasType(Spell);
    }

    public static boolean isRitual(Card card) {
        return card.hasType(Ritual);
    }

    public static boolean isAlly(Card card) {
        return card.hasType(Ally);
    }

    public static boolean isJunk(Card card) {
        return card.hasType(Junk);
    }

    public static boolean isUnit(Card card) {
        return card.hasType(Unit);
    }

    public static boolean isDamageable(Card c) {
        return c.combat != null;
    }

    public static boolean isStudyable(Card c) {
        return c.studiable != null;
    }

    public static boolean isDamageable(Object o) {
        return o instanceof Player || (o instanceof Card && ((Card) o).combat != null);
    }

    public static boolean isGreen(Card c) {
        return c.knowledge.containsKey(GREEN);
    }

    public static boolean any(Object o) {
        return true;
    }

    public static boolean none(Object o) {
        return false;
    }


}
