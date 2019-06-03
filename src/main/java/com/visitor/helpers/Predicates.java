/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.helpers;

import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.card.types.Junk;
import com.visitor.card.types.Ritual;
import com.visitor.card.types.Spell;
import com.visitor.game.Player;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public abstract class Predicates {

    public static boolean isItem(Card c) {
        return c instanceof Item;
    }
    
    public static boolean isSpell (Card c) {
        return c instanceof Spell;
    }
    
    public static boolean isRitual (Card c) {
        return c instanceof Ritual;
    }
    
    public static boolean isAlly(Card c) {
        return c instanceof Ally;
    }
    
    public static boolean isJunk(Card c) {
        return c instanceof Junk;
    }
    
    public static boolean isDamageable(Card c) {
        return c.isDamageable();
    }
    
    public static boolean isStudyable(Card c) {
        return !(c instanceof Junk);
    }
    
    public static boolean isDamageable(Object o) {
        return o instanceof Player || (o instanceof Card && ((Card)o).isDamageable());
    }
    
    public static boolean isGreen (Card c) {
        return c.knowledge.containsKey(GREEN);
    }

    public static boolean any(Object o) {
        return true;
    }
    
    public static boolean none(Object o) {
        return false;
    }

}
