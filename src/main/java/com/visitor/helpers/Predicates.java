/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.helpers;

import com.visitor.card.types.*;
import com.visitor.game.Player;

import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 * @author pseudo
 */
public abstract class Predicates {

    public static boolean isAsset(Card c) {
        return c.types.contains(Card.CardType.Asset);
    }

    public static boolean isSpell(Card c) {

        return c.types.contains(Card.CardType.Spell);
    }

    public static boolean isRitual(Card c) {

        return c.types.contains(Card.CardType.Ritual);
    }

    public static boolean isAlly(Card c) {

        return c.types.contains(Card.CardType.Ally);
    }

    public static boolean isJunk(Card c) {

        return c.types.contains(Card.CardType.Junk);
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
