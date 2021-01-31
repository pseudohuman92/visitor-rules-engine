package com.visitor.game;

import com.visitor.card.properties.Combat;
import com.visitor.helpers.*;
import com.visitor.helpers.containers.ActivatedAbility;
import com.visitor.helpers.containers.Damage;
import com.visitor.protocol.ServerGameMessages.*;
import com.visitor.protocol.Types.*;
import com.visitor.server.GameEndpoint;

import javax.websocket.EncodeException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.visitor.card.properties.Combat.CombatAbility.Deathtouch;
import static com.visitor.card.properties.Combat.CombatAbility.FirstStrike;
import static com.visitor.game.Event.*;
import static com.visitor.game.Game.Zone.*;
import static com.visitor.helpers.UUIDHelper.toUUIDList;
import static com.visitor.protocol.Types.Phase.*;
import static com.visitor.protocol.Types.SelectFromType.LIST;
import static com.visitor.protocol.Types.SelectFromType.NOTYPE;
import static com.visitor.server.GeneralEndpoint.gameServer;
import static java.lang.Math.random;
import static java.lang.System.out;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;


/**
 * @author pseudo
 */
public class Game implements Serializable {

	public transient Hashmap<UUID, GameEndpoint> connections;
	public UUID activePlayer;
	public transient ArrayBlockingQueue<Object> response;
	Hashmap<UUID, Player> players;
	Hashmap<UUID, ServerGameMessage> lastMessages;
	UUID turnPlayer;
	Arraylist<Card> stack;
	Phase phase;
	int turnCount;
	int passCount;
	UUID id;
	Hashmap<UUID, Arraylist<Card>> triggeringCards;
	Arraylist<Event> eventQueue;

	Arraylist<UUID> attackers;
	Arraylist<UUID> blockers;

	public Game () {
		id = randomUUID();
		players = new Hashmap<>();
		connections = new Hashmap<>();
		stack = new Arraylist<>();
		lastMessages = new Hashmap<>();
		response = new ArrayBlockingQueue<>(1);
		triggeringCards = new Hashmap<>();

		eventQueue = new Arraylist<>();
		attackers = new Arraylist<>();
		blockers = new Arraylist<>();
	}

	//This needs to be called to start the game.
	public void addPlayers (Player p1, Player p2) {
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
	}

	/**
	 * Connection Methods
	 * To deal with client connections
	 */
	public void addConnection (UUID playerId, GameEndpoint connection) {
		connections.putIn(playerId, connection);
	}

	public void removeConnection (UUID playerId) {
		connections.removeFrom(playerId);
	}

	public void setLastMessage (UUID playerId, ServerGameMessage lastMessage) {
		lastMessages.put(playerId, lastMessage);
	}

	public ServerGameMessage getLastMessage (UUID playerId) {
		return lastMessages.get(playerId);
	}

	public void addToResponseQueue (Object o) {
		try {
			response.put(o);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
	}


	/**
	 * Card Accessor Methods
	 * Getting Card objects from various places
	 */
	public Card getCard (UUID targetID) {
		for (Player player : players.values()) {
			Card c = player.getCard(targetID);
			if (c != null) {
				return c;
			}
		}
		for (Card c : stack) {
			if (c.id.equals(targetID)) {
				return c;
			}
		}
		return null;
	}

	private Card getCardFromZone (UUID playerId, Zone zone, UUID cardId) {
		Arraylist<Card> card = new Arraylist<>();
		getZone(playerId, zone).forEach(c -> {
			if (c.id.equals(cardId))
				card.add(c);
		});
		return card.size() > 0 ? card.get(0) : null;
	}

	public Arraylist<Card> getAllFrom (UUID playerId, Zone zone, Predicate<Card> pred) {
		Arraylist<Card> cards = new Arraylist<>();
		getZone(playerId, zone).forEach(c -> {
			if (pred.test(c)) {
				cards.add(c);
			}
		});
		return cards;
	}

	private Arraylist<Card> getAll (List<UUID> list) {
		return new Arraylist<>(list.stream().map(this::getCard).collect(Collectors.toList()));
	}

	public Card extractCard (UUID targetID) {
		for (Player player : players.values()) {
			Card c = player.extractCard(targetID);
			if (c != null) {
				return c;
			}
		}
		for (Card c : stack) {
			if (c.id.equals(targetID)) {
				stack.remove(c);
				return c;
			}
		}
		return null;
	}

	private Arraylist<Card> extractAll (List<UUID> list) {
		return new Arraylist<>(list.stream().map(this::extractCard).collect(Collectors.toList()));
	}

	private Arraylist<Card> extractAllCopiesFrom (UUID playerId, String cardName, Zone zone) {
		Arraylist<Card> cards = new Arraylist<>(getZone(playerId, zone).parallelStream()
				.filter(c -> c.name.equals(cardName)).collect(Collectors.toList()));
		getZone(playerId, zone).removeAll(cards);
		return cards;
	}


	/**
	 * Game Action Methods
	 * These are game actions taken by a client
	 */
	public void playCard (UUID playerId, UUID cardID, boolean withCost) {
		Card card = extractCard(cardID);
		card.play(withCost);
		addEvent(Event.playCard(card), true);
		activePlayer = getOpponentId(playerId);
	}

	public void playCard (UUID playerId, UUID cardID) {
		playCard(playerId, cardID, true);
	}

	public void playCardWithoutCost (UUID playerId, UUID cardID) {
		playCard(playerId, cardID, false);
	}

	public void activateCard (UUID playerId, UUID cardID) {
		getCard(cardID).activate();
		activePlayer = getOpponentId(playerId);
	}

	public void studyCard (UUID playerId, UUID cardID, boolean regular) {
		Card c = extractCard(cardID);
		c.study(getPlayer(playerId), regular);
		addEvent(Event.study(playerId, c), regular);
	}

	public void pass (UUID playerId) {
		passCount++;
		if (passCount == 2) {
			if (!stack.isEmpty()) {
				resolveStack();
			} else {
				changePhase();
			}
		} else {
			activePlayer = this.getOpponentId(playerId);
		}
	}

	public void redraw (UUID playerId) {
		getPlayer(playerId).redraw();
	}

	public void keep (UUID playerId) {
		passCount++;
		if (passCount == 2) {
			changePhase();
		} else {
			activePlayer = this.getOpponentId(playerId);
		}
	}


	/**
	 * Turn Structure Methods
	 * These are the methods that handle turn / phase transitions.
	 */
	@SuppressWarnings("DuplicateBranchesInSwitch")
	public void changePhase () {
		out.println("Changing Phase from " + phase);
		passCount = 0;
		activePlayer = turnPlayer;
		switch (phase) {
			case REDRAW:
				phase = BEGIN;
				newTurn();
				break;
			case BEGIN:
				phase = MAIN_BEFORE;
				break;
			case MAIN_BEFORE:
				activePlayer = UUID.randomUUID();
				phase = ATTACK;
				chooseAttackers();
				activePlayer = turnPlayer;
				if (attackers.isEmpty()) {
					phase = MAIN_AFTER;
				}
				break;
			case ATTACK:
				activePlayer = UUID.randomUUID();
				phase = BLOCK;
				chooseBlockers();
				activePlayer = this.getOpponentId(turnPlayer);
				break;
			case BLOCK:
				dealCombatDamage();
				unsetAttackers();
				unsetBlockers();
				phase = MAIN_AFTER;
				break;
			case MAIN_AFTER:
				phase = END;
				endTurn();
				break;
			case END:
				phase = BEGIN;
				newTurn();
				break;
		}
	}

	private void endTurn () {
		processEndEvents();
		players.values().forEach(Player::endTurn);
		if (getPlayer(turnPlayer).getHandSize() > 7) {
			discard(turnPlayer, getPlayer(turnPlayer).hand.size() - 7);
		}
		processEvents();
		changePhase();
	}

	private void newTurn () {
		if (turnCount > 0) {
			turnPlayer = this.getOpponentId(turnPlayer);
			getPlayer(turnPlayer).draw(1);
		}
		activePlayer = turnPlayer;
		passCount = 0;
		getPlayer(turnPlayer).draw(1);
		getPlayer(turnPlayer).newTurn();
		turnCount++;
		processBeginEvents();
		changePhase();
	}

	/**
	 * Event Related Methods
	 * These are the methods that implements event mechanism.
	 * Events are used to implement triggered abilities.
	 */
	public void addEvent (Event e, boolean process) {
		eventQueue.add(e);
		if (process)
			processEvents();
	}

	public void addEvent (Event e) {
		addEvent(e, false);
	}

	public void processEvents () {
		if (!eventQueue.isEmpty()) {
			Arraylist<Event> tempQueue = eventQueue;
			eventQueue = new Arraylist<>();
			while (!tempQueue.isEmpty()) {
				Event e = tempQueue.remove(0);
				out.println("Processing Event: " + e.type);
				triggeringCards.get(turnPlayer).forEachInOrder(c -> c.checkEvent(e));
				triggeringCards.get(this.getOpponentId(turnPlayer)).forEachInOrder(c -> c.checkEvent(e));
			}
		}
	}

	private void processBeginEvents () {
		//out.println("Starting Begin Triggers");
		addEvent(turnStart(turnPlayer), true);
		//out.println("Ending Begin Triggers");
	}

	private void processEndEvents () {
		//out.println("Starting End Triggers");
		addEvent(turnEnd(turnPlayer), true);
		//out.println("Ending End Triggers");
	}

	public void addTriggeringCard (UUID playerId, Card t) {
		triggeringCards.get(playerId).add(t);
	}

	public void removeTriggeringCard (Card card) {
		triggeringCards.values().forEach(l -> l.remove(card));
	}

	// Identifier accessor methods
	public UUID getOpponentId (UUID playerId) {
		for (UUID id : players.keySet()) {
			if (!id.equals(playerId)) {
				return id;
			}
		}
		return null;
	}

	/**
	 * Stack Methods
	 */
	public void addToStack (Card c) {
		passCount = 0;
		stack.add(0, c);
		processEvents();
	}

	// TODO: switch prevSize check to flag system
	private void resolveStack () {
		activePlayer = UUID.randomUUID();
		out.println("Updating players from resolveStack beginning. AP: " + activePlayer);
		updatePlayers();
		while (!stack.isEmpty() && passCount == 2) {
			Card c = stack.remove(0);
			int prevSize = stack.size();
			c.resolve();
			processEvents();
			if (stack.isEmpty() || prevSize != stack.size()) {
				passCount = 0;
				activePlayer = turnPlayer;
			} else {
				out.println("Updating players from resolveStack loop. AP: " + activePlayer);
				updatePlayers();
			}
		}
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


	/**
	 * Client Prompt Methods
	 * When you need client to do something.
	 */
	//// Helpers
	private void send (UUID playerId, ServerGameMessage.Builder builder) {
		try {
			setLastMessage(playerId, builder.build());
			GameEndpoint e = connections.get(playerId);
			if (e != null) {
				e.send(builder);
			}
		} catch (IOException | EncodeException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
	}

	private Arraylist<UUID> selectFrom (UUID playerId, SelectFromType type, Arraylist<Card> candidates, Arraylist<UUID> canSelect, Arraylist<UUID> canSelectPlayers, int count, boolean upTo, String message) {
		SelectFrom.Builder b = SelectFrom.newBuilder()
				.addAllSelectable(canSelect.transformToStringList())
				.addAllSelectable(canSelectPlayers.transformToStringList())
				.addAllCandidates(candidates.transform(c -> c.toCardMessage().build()))
				.setMessageType(type)
				.setSelectionCount(count)
				.setUpTo(upTo)
				.setMessage(message)
				.setGame(toGameState(playerId, true));
		try {
			send(playerId, ServerGameMessage.newBuilder().setSelectFrom(b));
			String[] l = (String[]) response.take();
			return toUUIDList(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	private Arraylist<AttackerAssignment> selectAttackers (UUID playerId) {

		List<String> attackers = getZone(playerId, Zone.Play).parallelStream()
				.filter(Card::canAttack)
				.map(u -> u.id.toString()).collect(Collectors.toList());

		if (attackers.isEmpty()) {
			return (new Arraylist<>());
		}
		Arraylist<String> targets = new Arraylist<>(getOpponentId(playerId).toString());
		List<String> allies = getZone(this.getOpponentId(playerId), Zone.Play).parallelStream()
				.filter(Predicates::isAlly)
				.map(u -> u.id.toString()).collect(Collectors.toList());
		targets.addAll(allies);
		List<Attacker> attackerList = attackers.parallelStream()
				.map(a -> Attacker.newBuilder()
						.setAttackerId(a)
						.addAllPossibleAttackTargets(targets).build())
				.collect(Collectors.toList());
		out.println("Sending Select Attackers Message to " + playerId);
		SelectAttackers.Builder b = SelectAttackers.newBuilder()
				.addAllPossibleAttackers(attackerList)
				.setGame(toGameState(playerId, true));
		try {
			send(playerId, ServerGameMessage.newBuilder().setSelectAttackers(b));
			AttackerAssignment[] l = (AttackerAssignment[]) response.take();
			return new Arraylist<>(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	private Arraylist<BlockerAssignment> selectBlockers (UUID playerId) {

		List<Card> potentialBlockers =
				getZone(playerId, Zone.Play)
						.parallelStream()
						.filter(Card::canBlock)
						.collect(Collectors.toList());

		if (potentialBlockers.isEmpty()) {
			return (new Arraylist<>());
		}

		Arraylist<Blocker> blockers = new Arraylist<>();
		potentialBlockers.forEach(pb -> {
			List<String> targets = attackers.parallelStream()
					.filter(a -> pb.canBlock(getCard(a)))
					.map(u -> getCard(u).id.toString())
					.collect(Collectors.toList());
			if (!targets.isEmpty()) {
				blockers.add(Blocker.newBuilder()
						.setBlockerId(pb.id.toString())
						.addAllPossibleBlockTargets(targets)
						.build());
			}
		});

		if (blockers.isEmpty()) {
			return (new Arraylist<>());
		}

		out.println("Sending Select Blockers Message to " + playerId);
		SelectBlockers.Builder b = SelectBlockers.newBuilder()
				.addAllPossibleBlockers(blockers)
				.setGame(toGameState(playerId, true));
		try {
			send(playerId, ServerGameMessage.newBuilder().setSelectBlockers(b));
			BlockerAssignment[] l = (BlockerAssignment[]) response.take();
			return new Arraylist<>(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	private Arraylist<DamageAssignment> assignDamage (UUID playerId, UUID id, Arraylist<UUID> possibleTargets, int damage, boolean trample) {
		out.println("Sending Assign Damage Message to " + playerId);
		AssignDamage.Builder b = AssignDamage.newBuilder()
				.setDamageSource(id.toString())
				.addAllPossibleTargets(possibleTargets.transformToStringList())
				.setTotalDamage(damage)
				.setTrample(trample)
				.setGame(toGameState(playerId, true));
		try {
			send(playerId, ServerGameMessage.newBuilder().setAssignDamage(b));
			DamageAssignment[] l = (DamageAssignment[]) response.take();
			return new Arraylist<>(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	//// Prompters
	public Arraylist<UUID> selectFromZone (UUID playerId, Zone zone, Predicate<Card> validTarget, int count, boolean upTo, String message) {
		Arraylist<UUID> canSelect = new Arraylist<>(getZone(playerId, zone).parallelStream()
				.filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
		return selectFrom(playerId, getZoneLabel(zone), getZone(playerId, zone), canSelect, new Arraylist<>(), count, upTo, message);
	}

	public Arraylist<UUID> selectFromZoneWithPlayers (UUID playerId, Zone zone, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo, String message) {
		Arraylist<UUID> canSelect = new Arraylist<>(getZone(playerId, zone).parallelStream()
				.filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
		Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
				.filter(validPlayer).map(c -> c.id).collect(Collectors.toList()));
		return selectFrom(playerId, getZoneLabel(zone), getZone(playerId, zone), canSelect, canSelectPlayers, count, upTo, message);
	}

	public Arraylist<UUID> selectFromList (UUID playerId, Arraylist<Card> candidates, Predicate<Card> validTarget, int count, boolean upTo, String message) {
		if (message == null || message.equals("")){
			message = "Select " + (upTo? "up to " : "") + count;
		}
		Arraylist<UUID> canSelect = new Arraylist<>(candidates.parallelStream()
				.filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
		return selectFrom(playerId, LIST, candidates, canSelect, new Arraylist<>(), count, upTo, message);
	}

	public Arraylist<UUID> selectPlayers (UUID playerId, Predicate<Player> validPlayer, int count, boolean upTo) {
		Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
				.filter(validPlayer).map(c -> c.id).collect(Collectors.toList()));
		String message = "Select " + (upTo ? " up to " : "") + count + " player" + (count > 1 ? "s." : ".");
		return selectFrom(playerId, getZoneLabel(Zone.Play), new Arraylist<>(), new Arraylist<>(), canSelectPlayers, count, upTo, message);
	}

	public Arraylist<UUID> selectDamageTargetsConditional (UUID playerId, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo, String message) {
		return selectFromZoneWithPlayers(playerId, Both_Play, validTarget, validPlayer, count, upTo, message);
	}

	public Arraylist<UUID> selectDamageTargets (UUID playerId, int count, boolean upTo, String message) {
		return selectFromZoneWithPlayers(playerId, Both_Play, Predicates::isDamageable, Predicates::any, count, upTo, message);
	}

	public void assignDamage (UUID id, Arraylist<UUID> possibleTargets, Damage damage, boolean trample) {
		out.println("Updating players from assignDamage. AP: " + activePlayer);
		updatePlayers();
		Arraylist<DamageAssignment> assignedDamages = assignDamage(turnPlayer, id, possibleTargets, damage.amount, trample);
		out.println("Damage distribution: " + assignedDamages);
		assignedDamages.forEach(c -> {
			UUID targetId = fromString(c.getTargetId());
			int assignedDamage = c.getDamage();
			dealDamage(id, targetId, new Damage(assignedDamage, damage.mayKill, damage.combat));
		});
	}

	public int selectX (UUID playerId, int maxX) {
		if (maxX == 0) {
			return maxX;
		}
		SelectXValue.Builder b = SelectXValue.newBuilder()
				.setMaxXValue(maxX)
				.setGame(toGameState(playerId, true));
		try {
			send(playerId, ServerGameMessage.newBuilder().setSelectXValue(b));

			return (int) response.take();
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return 0;
	}

	public CounterMap<Knowledge> selectKnowledge (UUID playerId, Set<Knowledge> knowledgeSet) {
		SelectKnowledge.Builder b = SelectKnowledge.newBuilder()
				.addAllKnowledgeList(knowledgeSet)
				.setGame(toGameState(playerId, true));
		out.println(b.build());
		try {
			send(playerId, ServerGameMessage.newBuilder().setSelectKnowledge(b));
			return new CounterMap<>((Knowledge) response.take(), 1);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return new CounterMap<>();
	}


	// Unsorted methods
	private Arraylist<Card> getZone (UUID playerId, Zone zone) {
		switch (zone) {
			case Deck:
				return getPlayer(playerId).deck;
			case Hand:
				return getPlayer(playerId).hand;
			case Play:
				return getPlayer(playerId).playArea;
			case Discard_Pile:
				return getPlayer(playerId).discardPile;
			case Void:
				return getPlayer(playerId).voidPile;
			case Stack:
				return stack;
			case Opponent_Play:
				return getPlayer(getOpponentId(playerId)).playArea;
			case Opponent_Hand:
				return getPlayer(getOpponentId(playerId)).hand;
			case Opponent_Discard_Pile:
				return getPlayer(getOpponentId(playerId)).discardPile;
			case Both_Play:
				Arraylist<Card> total = new Arraylist<>();
				players.values().forEach(player -> total.addAll(player.playArea));
				return total;
			default:
				return null;
		}
	}

	public boolean hasIn (UUID playerId, Zone zone, Predicate<Card> validTarget, int count) {
		return getZone(playerId, zone).parallelStream().filter(validTarget).count() >= count;
	}

	public void putTo (UUID playerId, Card c, Zone zone) {
		getZone(playerId, zone).add(c);
	}

	public void putTo (UUID playerId, Card c, Zone zone, int index) {
		getZone(playerId, zone).add(index, c);
	}

	public void putTo (UUID playerId, Arraylist<Card> cards, Zone zone) {
		getZone(playerId, zone).addAll(cards);
	}

	public void addEnergy (UUID playerId, int i) {
		getPlayer(playerId).energy += i;
	}

	public void spendEnergy (UUID playerId, int i) {
		getPlayer(playerId).energy -= i;
	}

	public void draw (UUID playerId, int count) {
		Player player = getPlayer(playerId);
		player.draw(count);
		if (player.deck.isEmpty()) {
			gameEnd(playerId, false);
		}
	}

	public void draw (UUID playerId, UUID cardID) {
		getPlayer(playerId).hand.add(extractCard(cardID));
	}

	public void draw (UUID playerId, Card card) {
		getPlayer(playerId).hand.add(card);
	}

	public void purge (UUID playerId, UUID cardID) {
		getPlayer(playerId).voidPile.add(extractCard(cardID));
	}

	public void destroy (UUID targetId) {
		destroy(null, targetId);
	}

	public void destroy (UUID sourceId, UUID targetId) {
		Card c = getCard(targetId);
		if (sourceId != null) {
			addEvent(Event.destroy(getCard(sourceId), c));
		} else {
			addEvent(Event.destroy(null, c));
		}
		c.destroy();
	}

	public void loot (UUID playerId, int x) {
		Game.this.draw(playerId, x);
		discard(playerId, x);
	}

	public void discard (UUID playerId, int count) {
		if (!getZone(playerId, Hand).isEmpty()) {
			String message = "Discard " + (count > 1 ? count + " cards." : "a card.");
			Arraylist<Card> d = getPlayer(playerId).discardAll(selectFromZone(playerId, Zone.Hand, Predicates::any, Math.min(count, getZone(playerId, Hand).size()), false, message));
			addEvent(Event.discard(playerId, d));
		}
	}

	public void discard (UUID playerId, UUID cardID) {
		getPlayer(playerId).discard(cardID);
		addEvent(Event.discard(playerId, getCard(cardID)));
	}

	public void discardAll (UUID playerId, Arraylist<Card> cards) {
		getPlayer(playerId).discardAll(UUIDHelper.toUUIDList(cards));
		addEvent(Event.discard(playerId, cards));
	}

	public void deplete (UUID id) {
		getCard(id).deplete();
	}

	public void ready (UUID id) {
		getCard(id).newTurn();
	}

	public boolean ownedByOpponent (UUID targetID) {
		Card c = getCard(targetID);
		return c.owner.equals(this.getOpponentId(c.controller));
	}

	public void payHealth (UUID playerId, int count) {
		getPlayer(playerId).payHealth(count);
	}

	public void possessTo (UUID newController, UUID cardID, Zone zone) {
		Card c = extractCard(cardID);
		UUID oldController = c.controller;
		c.controller = newController;
		getZone(newController, zone).add(c);
	}

	public boolean controlsUnownedCard (UUID playerId, Zone zone) {
		return getZone(playerId, zone).parallelStream().anyMatch(c -> ownedByOpponent(c.id));
	}

	public boolean isIn (UUID playerId, Zone zone, UUID cardID) {
		return getZone(playerId, zone).parallelStream().anyMatch(getCard(cardID)::equals);
	}


	public int getMaxEnergy (UUID playerId) {
		return getPlayer(playerId).maxEnergy;
	}

	public void addStudyCount (UUID playerId, int count) {
		getPlayer(playerId).numOfStudiesLeft += count;
	}

	public void sacrifice (UUID cardId) {
		Card c = getCard(cardId);
		c.sacrifice();
		addEvent(Event.sacrifice(c));
	}

	public void gameEnd (UUID playerId, boolean win) {
		send(playerId, ServerGameMessage.newBuilder()
				.setGameEnd(GameEnd.newBuilder()
						.setGame(toGameState(playerId, true))
						.setWin(win)));
		send(getOpponentId(playerId), ServerGameMessage.newBuilder()
				.setGameEnd(GameEnd.newBuilder()
						.setGame(toGameState(getOpponentId(playerId), true))
						.setWin(!win)));
		connections.forEach((s, c) -> c.close());
		connections = new Hashmap<>();
		gameServer.removeGame(id);
	}




	public final void updatePlayers () {
		players.forEach((name, player) -> send(name, ServerGameMessage.newBuilder()
				.setUpdateGameState(UpdateGameState.newBuilder()
						.setGame(toGameState(name, false)))));
	}

	public boolean hasEnergy (UUID playerId, int i) {
		return getPlayer(playerId).energy >= i;
	}

	public boolean hasKnowledge (UUID playerId, CounterMap<Knowledge> knowledge) {
		return getPlayer(playerId).hasKnowledge(knowledge);
	}

	public boolean canPlaySlow (UUID playerId) {
		return turnPlayer.equals(playerId)
				&& activePlayer.equals(playerId)
				&& stack.isEmpty()
				&& (phase == MAIN_BEFORE || phase == MAIN_AFTER);
	}

	public UUID getId () {
		return id;
	}

	public boolean canStudy (UUID playerId) {
		return canPlaySlow(playerId)
				&& getPlayer(playerId).numOfStudiesLeft > 0;
	}

	public void shuffleIntoDeck (UUID playerId, Card... cards) {
		getPlayer(playerId).shuffleIntoDeck(cards);
	}

	private SelectFromType getZoneLabel (Zone zone) {
		switch (zone) {
			case Hand:
				return SelectFromType.HAND;
			case Play:
			case Both_Play:
			case Opponent_Play:
				return SelectFromType.PLAY;
			case Discard_Pile:
			case Opponent_Discard_Pile:
				return SelectFromType.DISCARD_PILE;
			case Void:
				return SelectFromType.VOID;
			case Stack:
				return SelectFromType.STACK;
			case Deck:
			case Opponent_Hand:
				return SelectFromType.LIST;
			default:
				return NOTYPE;
		}
	}


	public boolean isPlayerActive (UUID playerId) {
		return activePlayer.equals(playerId);
	}

	public boolean isPlayerInGame (String username) {
		return getPlayerId(username) != null;
	}


	// Player property getters
	private Player getPlayer (UUID playerId) {
		return players.get(playerId);
	}

	public int getPlayerEnergy (UUID playerId) {
		return getPlayer(playerId).energy;
	}

	public UUID getPlayerId (String username) {
		for (Player p : players.values()){
			if (p.username.equals(username)) return p.id;
		}
		return null;
	}

	// Does nothing if called with non-positive amount of damage
	public void dealDamage (UUID sourceId, UUID targetId, Damage damage) {
		if (damage.amount > 0) {
			Card source = getCard(sourceId);
			if (isPlayer(targetId)) {
				getPlayer(targetId).receiveDamage(damage.amount, source);
			} else {
				Card c = getCard(targetId);
				if (c != null) {
					c.receiveDamage(damage, source);

				}
			}
			source.combat.triggerDamageEffects(targetId, damage);
		}
	}

	public void dealDamage (UUID sourceId, UUID targetId, int damage) {
		dealDamage(sourceId, targetId, new Damage(damage));
	}

	public boolean isTurnPlayer (UUID playerId) {
		return turnPlayer.equals(playerId);
	}

	public boolean hasMaxEnergy (UUID playerId, int count) {
		return getPlayer(playerId).maxEnergy >= count;
	}

	public void removeMaxEnergy (UUID playerId, int count) {
		getPlayer(playerId).maxEnergy -= count;
	}

	public boolean isPlayer (UUID targetId) {
		return players.values().stream().anyMatch(p -> p.id.equals(targetId));
	}


	public void dealDamageToAll (UUID playerId, UUID sourceId, Damage damage) {
		players.values().forEach(p -> dealDamage(sourceId, p.id, damage));
		getZone(playerId, Both_Play).forEach(c -> dealDamage(sourceId, c.id, damage));
	}

	public void donate (UUID donatedCardId, UUID newController, Zone newZone) {
		Card c = extractCard(donatedCardId);
		UUID oldController = c.controller;
		c.controller = newController;
		getZone(newController, newZone).add(c);
	}

	/**
	 * Combat Helper Methods
	 */
	private void chooseAttackers () {
		out.println("Updating players from chooseAttackers. AP: " + activePlayer);
		updatePlayers();
		Arraylist<AttackerAssignment> attackerIds = selectAttackers(turnPlayer);
		out.println("Attackers: " + attackerIds);
		Arraylist<Card> attackingCards = new Arraylist<>();
		attackerIds.forEach(c -> {
			Card u = getCard(fromString(c.getAttackerId()));
			u.setAttacking(fromString(c.getAttacksTo()));
			attackers.add(u.id);
			attackingCards.add(u);
		});
		addEvent(Event.attack(attackingCards), true);
	}

	private void chooseBlockers () {
		out.println("Updating players from chooseBlockers. AP: " + activePlayer);
		updatePlayers();
		Arraylist<BlockerAssignment> assignedBlockers = selectBlockers(this.getOpponentId(turnPlayer));
		out.println("Blockers: " + assignedBlockers);
		assignedBlockers.forEach(c -> {
			UUID blockerId = fromString(c.getBlockerId());
			UUID blockedBy = fromString(c.getBlockedBy());

			getCard(blockedBy).addBlocker(blockerId);
			Card blocker = getCard(blockerId);
			blocker.setBlocking(blockedBy);
			blockers.add(blockerId);
		});

	}

	private void dealCombatDamage () {
		ArrayList<Card> firstStrikeAttackers = new Arraylist<>(getAll(attackers));
		firstStrikeAttackers.removeIf(a -> !a.hasCombatAbility(FirstStrike));
		firstStrikeAttackers.forEach(a -> a.dealAttackDamage(true));

		processEvents();

		ArrayList<Card> normalAttackers = new Arraylist<>(getAll(attackers));
		normalAttackers.removeIf(a -> a.hasCombatAbility(FirstStrike));
		ArrayList<Card> normalBlockers = new Arraylist<>(getAll(blockers));

		normalAttackers.forEach(a -> a.dealAttackDamage(false));
		normalBlockers.forEach(Card::dealBlockDamage);

		//TODO: these kill if they blocked by a deathtouch unit even if it is not damaged by it.
		normalAttackers.forEach(Card::maybeDieFromBlock);
		normalBlockers.forEach(Card::maybeDieFromAttack);

		processEvents();
	}

	private void unsetAttackers () {
		attackers.forEach(u -> getCard(u).unsetAttacking());
		attackers.clear();
	}

	private void unsetBlockers () {
		blockers.forEach(u -> getCard(u).unsetBlocking());
		blockers.clear();
	}


	public void saveGameState (String filename) {
		try {

			FileOutputStream fileOut = new FileOutputStream(filename + ".gamestate");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(this);
			objectOut.close();
			System.out.println("Game state is succesfully written to a file");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void returnAllCardsToHand () {
		forEachInZone(UUID.randomUUID(), Both_Play, Predicates::any, this::returnToHand);
	}

	public void heal (UUID cardId, int healAmount) {
		getCard(cardId).heal(healAmount);
	}

	private Card createFreshCopy (Card c) {
		try {
			Class<?> cardClass = c.getClass();
			Constructor<?> cardConstructor = cardClass.getConstructor(Game.class, String.class);
			Object card = cardConstructor.newInstance(this, c.controller);
			return ((Card) card);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			getLogger(Deck.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	public Card restore (UUID cardId) {
		return createFreshCopy(extractCard(cardId));
	}

	public void resurrect (UUID cardId) {
		HelperFunctions.runIfNotNull(getCard(cardId), () -> restore(cardId).resolve());
	}


	public void putToBottomOfDeck (UUID cardId) {
		Card card = extractCard(cardId);
		getPlayer(card.controller).putToBottomOfDeck(card);
	}

	public void destroyAllUnits () {
		getZone(null, Both_Play).forEach(c -> {
			if (Predicates.isUnit(c)) {
				destroy(c.id);
			}
		});
	}

	public void cancel (UUID cardId) {
		Card card = extractCard(cardId);
		card.clear();
		putTo(card.controller, card, Discard_Pile);
	}

	public void putToTopOfDeck (UUID cardId) {
		Card card = extractCard(cardId);
		getPlayer(card.controller).putToBottomOfDeck(card);
	}

	public void putToBottomOfDeck (UUID playerId, Arraylist<Card> toBottom) {
		Player player = getPlayer(playerId);
		toBottom.forEach(player::putToBottomOfDeck);
	}

	public Arraylist<Card> extractFromTopOfDeck (UUID playerId, int count) {
		return getPlayer(playerId).extractFromTopOfDeck(count);
	}

	public void forEachInZone (UUID playerId, Zone zone, Predicate<Card> filter, Consumer<UUID> cardIdConsumer) {
		getZone(playerId, zone).forEach(card -> {
			if(filter.test(card)){
				cardIdConsumer.accept(card.id);
			}
		});
	}

	public int countInZone (UUID playerId, Zone zone, Predicate<Card> cardConsumer) {
		Arraylist<Integer> count = new Arraylist<>();
		getZone(playerId, zone).forEach(card -> {
			if (cardConsumer.test(card)) {
				count.add(0);
			}
		});
		return count.size();
	}

	public void shuffleDeck (UUID playerId) {
		getPlayer(playerId).shuffleDeck();
	}

	public Card extractTopmostMatchingFromDeck (UUID playerId, Predicate<Card> cardPredicate) {
		return getPlayer(playerId).extractTopmostMatchingFromDeck(cardPredicate);
	}

	public void gainControlFromZone (UUID newController, Zone oldZone, Zone newZone, UUID targetId) {
		runIfInZone(newController, oldZone, targetId,
				()->{
			Card c = extractCard(targetId);
			c.controller = newController;
			putTo(newController, c, newZone);
		});
	}

	public void runIfInZone(UUID playerId, Zone zone, UUID cardId, Runnable function){
		if(isIn(playerId, zone, cardId)){
			function.run();
		}
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

	public void returnToHand (UUID cardId) {
		getCard(cardId).returnToHand();
	}

	public int getAttack (UUID cardId) {
		return getCard(cardId).getAttack();
	}

	public void runIfHasKnowledge (UUID playerId, CounterMap<Knowledge> knowledge, Runnable effect) {
		if (getPlayer(playerId).hasKnowledge(knowledge)){
			effect.run();
		}
	}

	public boolean hasHealth (UUID playerId, int i) {
		return getPlayer(playerId).hasHealth(i);
	}

	public int getDeckSize (UUID playerId) {
		return getPlayer(playerId).deck.size();
	}

	public void purgeFromDeck (UUID playerId, int i) {
		getPlayer(playerId).purgeFromDeck(i);
	}

	public void addTurnlyActivatedAbility (UUID cardId, ActivatedAbility ability) {
		getCard(cardId).addTurnlyActivatedAbility(ability);
	}

	public Card discardAtRandom (UUID playerId) {
		return getPlayer(playerId).discardAtRandom();
	}

	public void runIfInPlay (UUID cardId, Runnable r) {
		runIfInZone(UUID.randomUUID(), Both_Play, cardId, r);
	}

	public int getHealth (UUID cardId) {
		return getCard(cardId).getHealth();
	}

	public void purge (UUID cardId) {
		getCard(cardId).purge();
	}

	public void addCombatAbility (UUID cardId, Combat.CombatAbility combatAbility) {
		getCard(cardId).addCombatAbility(combatAbility);
	}

	public void addShield (UUID cardId, int i) {
		getCard(cardId).addShield(i);
	}

	public void fight (UUID cardId, UUID targetId) {
		Card c1 = getCard(cardId);
		Card c2 = getCard(targetId);

		dealDamage(c1.id, c2.id, new Damage(c1.getAttack(), false, false));
		dealDamage(c2.id, c1.id, new Damage(c2.getAttack(), false, false));

		if (c2.getHealth() == 0 || c1.hasCombatAbility(Deathtouch))
			destroy(c2.id);
		if (c1.getHealth() == 0 || c2.hasCombatAbility(Deathtouch))
			destroy(c1.id);
	}

	public Arraylist<Card> getTopCardsFromDeck (UUID playerId, int i) {
		return getPlayer(playerId).getFromTopOfDeck(i);
	}

	public GameState.Builder toGameState (UUID playerId, boolean noAction) {
		GameState.Builder b =
				GameState.newBuilder()
						.setId(id.toString())
						.setPlayer(getPlayer(playerId).toPlayerMessage(true))
						.setOpponent(getPlayer(this.getOpponentId(playerId)).toPlayerMessage(false))
						.setTurnPlayer(turnPlayer.toString())
						.setActivePlayer(activePlayer.toString())
						.setPhase(phase);
		for (Card card : stack) {
			b.addStack(card.toCardMessage());
		}

		for (UUID attacker : attackers) {
			b.addAttackers(attacker.toString());
		}

		for (UUID blocker : blockers) {
			b.addBlockers(blocker.toString());
		}

	if(noAction)
		return b;

		players.forEach((s, p) -> {
			if (playerId.equals(s) && isPlayerActive(s)) {
				p.hand.forEach(c -> {
					if (c.canPlay(true)) {
						b.addCanPlay(c.id.toString());
					}
					if (c.canStudy()) {
						b.addCanStudy(c.id.toString());
					}
				});
				p.playArea.forEach(c -> {
					if (c.canActivate()) {
						b.addCanActivate(c.id.toString());
					}
				});
			}
		});
		return b;
	}

	public GameState.Builder toGameState (String username) {
		return toGameState(getPlayerId(username), false);
	}

	public void addAttachmentTo (UUID attachedId, UUID attachmentId) {
		getCard(attachedId).addAttachment(attachmentId);
	}

	public void removeAttachmentFrom (UUID attachedTo, UUID attachmentId) {
		getCard(attachedTo).removeAttachment(attachmentId);
	}

	public void removeAttackAndHealth (UUID cardId, int attack, int health) {
		getCard(cardId).loseAttack(attack);
		getCard(cardId).loseHealth(health);
	}

	public void gainHealth (UUID playerId, int health) {
		getPlayer(playerId).addHealth(health);
	}

	public enum Zone {
		Deck, Hand, Opponent_Hand, Play, Opponent_Play, Both_Play, Discard_Pile, Void, Opponent_Discard_Pile, Stack
	}

}
