/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.helpers;

import com.visitor.game.Card;
import com.visitor.game.Player;

import java.util.function.Predicate;

import static com.visitor.game.Card.CardSubtype.*;
import static com.visitor.game.Card.CardType.*;
import static com.visitor.protocol.Types.Knowledge.*;

/**
 * @author pseudo
 */
public abstract class Predicates {

	public static boolean isAsset (Card card) {
		return card.hasType(Asset);
	}

	public static boolean isSpell (Card card) {
		return card.hasType(Spell);
	}

	public static boolean isCantrip (Card card) {
		return card.hasSubtype(Cantrip);
	}

	public static boolean isRitual (Card card) {
		return card.hasSubtype(Ritual);
	}

	public static boolean isAlly (Card card) {
		return card.hasType(Ally);
	}

	public static boolean isJunk (Card card) {
		return card.hasType(Junk);
	}

	public static boolean isUnit (Card card) {
		return card.hasType(Unit);
	}

	public static boolean isDamageable (Card c) {
		return c.isDamagable();
	}

	public static boolean isStudyable (Card c) {
		return c.isStudiable();
	}

	public static boolean isDamageable (Object o) {
		return o instanceof Player || (o instanceof Card && ((Card) o).isDamagable());
	}

	public static boolean isGreen (Card c) {
		return c.hasColor(GREEN);
	}

	public static boolean any (Object o) {
		return true;
	}

	public static boolean none (Object o) {
		return false;
	}


	public static <T> Predicate<T> and(Predicate<T> ...predicates){
		return (t -> {
			for (Predicate<T> predicate : predicates) {
				if(!predicate.test(t))
					return false;
			}
			return true;
		});
	}

	public static <T> Predicate<T> or(Predicate<T> ...predicates){
		return (t -> {
			for (Predicate<T> predicate : predicates) {
				if(predicate.test(t));
					return true;
			}
			return false;
		});
	}

	public static <T> Predicate<T> not(Predicate<T> predicate){
		return (t -> !predicate.test(t));
	}

	public static boolean isPurple (Card card) {
		return card.hasColor(PURPLE);
	}

	public static boolean isRed (Card card) {
		return card.hasColor(RED);
	}
}
