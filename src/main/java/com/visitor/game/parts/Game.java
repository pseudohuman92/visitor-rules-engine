package com.visitor.game.parts;

import com.visitor.card.properties.Combat;
import com.visitor.helpers.Arraylist;
import com.visitor.protocol.Types.Knowledge;

import java.util.UUID;

import static com.visitor.protocol.Types.Phase.REDRAW;
import static java.lang.Math.random;
import static java.lang.System.out;


/**
 * @author pseudo
 * Base -> Connection -> Accessor -> Messaging -> Events ->
 * Stack ->  Combat ->  Actions -> TurnStrucrture -> ClientActions -> Game
 */
public class Game extends GameClientActionsPart {

	//This needs to be called to start the game.
	public void addPlayers (com.visitor.game.Player p1, com.visitor.game.Player p2) {
		triggeringCards.put(p1.id, new Arraylist<>());
		triggeringCards.put(p2.id, new Arraylist<>());

		players.put(p1.id, p1);
		players.put(p2.id, p2);

		p1.deck.shuffle();
		p2.deck.shuffle();

		phase = REDRAW;
		turnPlayer = (random() < 0.5) ? p1.id : p2.id;
		activePlayer = turnPlayer;
		turnCount = 0;
		passCount = 0;
		p1.draw(5);
		p2.draw(5);
		out.println("Updating players from Game addPlayers. AP: " + activePlayer);
		updatePlayers();
		startActiveClock();
	}












	/*
    // This is stop after each resolution version.
    private void resolveStack() {
        if (passCount == 2) {
            activePlayer = " ";
            updatePlayers();
            Card c = stack.remove(0);
            c.resolve(this);
            passCount = 0;
            activePlayer = turnPlayer;
        }
    }
    */

	/**
	 * Transformation Methods
	 * To change one card to another in-place.

	private void replaceWith (Card oldCard, Card newCard) {
		players.values().forEach(p -> p.replaceCardWith(oldCard, newCard));
		for (int i = 0; i < stack.size(); i++) {
			if (stack.get(i).equals(oldCard)) {
				stack.remove(i);
				stack.add(i, newCard);
			}
		}
	}

	public void transformTo (Card transformingCard, Card transformedCard, Card transformTo) {
		replaceWith(transformedCard, transformTo);
		addEvent(Event.transform(transformingCard, transformedCard, transformTo));
	}


	public void transformToJunk (Card transformingCard, UUID cardID) {
		Card card = getCard(cardID);
		Junk junk = new Junk(this, card.controller);
		junk.copyPropertiesFrom(card);
		transformTo(transformingCard, card, junk);
	}
	 */





	// Unsorted methods




	public boolean ownedByOpponent (UUID targetID) {
		com.visitor.game.Card c = getCard(targetID);
		return c.owner.equals(this.getOpponentId(c.controller));
	}



	public boolean controlsUnownedCard (UUID playerId, Zone zone) {
		return getZone(playerId, zone).parallelStream().anyMatch(c -> ownedByOpponent(c.id));
	}




	public int getMaxEnergy (UUID playerId) {
		return getPlayer(playerId).maxEnergy;
	}



















	public boolean hasMaxEnergy (UUID playerId, int count) {
		return getPlayer(playerId).maxEnergy >= count;
	}










	public void shuffleDeck (UUID playerId) {
		getPlayer(playerId).shuffleDeck();
	}



	public void addTurnlyCombatAbility (UUID targetId, Combat.CombatAbility combatAbility) {
		getCard(targetId).addTurnlyCombatAbility(combatAbility);
	}

	public void setAttackAndHealth (UUID cardId, int attack, int health) {
		getCard(cardId).setAttack(attack);
		getCard(cardId).setHealth(health);
	}

	public void addTurnlyAttackAndHealth (UUID cardId, int attack, int health) {
		getCard(cardId).addTurnlyAttackAndHealth(attack, health);
	}

	public void addAttackAndHealth (UUID cardId, int attack, int health) {
		getCard(cardId).addAttackAndHealth(attack, health);
	}

	public int getKnowledgeCount (UUID playerId, Knowledge knowledge) {
		return getPlayer(playerId).getKnowledgeCount(knowledge);
	}


	public int getAttack (UUID cardId) {
		return getCard(cardId).getAttack();
	}



	public boolean hasHealth (UUID playerId, int i) {
		return getPlayer(playerId).hasHealth(i);
	}

	public int getDeckSize (UUID playerId) {
		return getPlayer(playerId).deck.size();
	}

	public int getHealth (UUID cardId) {
		return getCard(cardId).getHealth();
	}











	public void stopActiveClock () {
		if (activePlayer != null){
			getPlayer(activePlayer).clock.pause();
		}
	}

	public void startActiveClock () {
		if (activePlayer != null){
			getPlayer(activePlayer).clock.activate();
		}
	}





}
