package com.visitor.game;

import com.visitor.card.types.Junk;
import com.visitor.helpers.*;
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
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.visitor.card.properties.Combat.CombatAbility.FirstStrike;
import static com.visitor.game.Event.*;
import static com.visitor.game.Game.Zone.Both_Play;
import static com.visitor.game.Game.Zone.Discard_Pile;
import static com.visitor.helpers.UUIDHelper.toUUIDList;
import static com.visitor.protocol.Types.Phase.*;
import static com.visitor.protocol.Types.SelectFromType.LIST;
import static com.visitor.protocol.Types.SelectFromType.NOTYPE;
import static com.visitor.server.GeneralEndpoint.gameServer;
import static java.lang.Math.random;
import static java.lang.System.out;
import static java.util.UUID.randomUUID;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;


/**
 * @author pseudo
 */
public class Game implements Serializable {

	public transient Hashmap<String, GameEndpoint> connections;
	public String activePlayer;
	public transient ArrayBlockingQueue<Object> response;
	Hashmap<String, Player> players;
	Hashmap<String, ServerGameMessage> lastMessages;
	String turnPlayer;
	Arraylist<Card> stack;
	Phase phase;
	int turnCount;
	int passCount;
	UUID id;
	Hashmap<String, Arraylist<Card>> triggeringCards;
	Arraylist<Event> eventQueue;
	boolean endProcessed;

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
		triggeringCards.put(p1.username, new Arraylist<>());
		triggeringCards.put(p2.username, new Arraylist<>());

		players.put(p1.username, p1);
		players.put(p2.username, p2);

		p1.deck.shuffle();
		p2.deck.shuffle();

		phase = REDRAW;
		turnPlayer = (random() < 0.5) ? p1.username : p2.username;
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
	public void addConnection (String username, GameEndpoint connection) {
		connections.putIn(username, connection);
	}

	public void removeConnection (String username) {
		connections.removeFrom(username);
	}

	public void setLastMessage (String username, ServerGameMessage lastMessage) {
		lastMessages.put(username, lastMessage);
	}

	public ServerGameMessage getLastMessage (String username) {
		return lastMessages.get(username);
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

	public Card getCardFromZone (String username, Zone zone, UUID cardId) {
		Arraylist<Card> card = new Arraylist<>();
		getZone(username, zone).forEach(c -> {
			if (c.id.equals(cardId))
				card.add(c);
		});
		return card.size() > 0 ? card.get(0) : null;
	}

	public Arraylist<Card> getAllFrom (String username, Zone zone, Predicate<Card> pred) {
		Arraylist<Card> cards = new Arraylist<>();
		getZone(username, zone).forEach(c -> {
			if (pred.test(c)) {
				cards.add(c);
			}
		});
		return cards;
	}

	public Arraylist<Card> getAll (List<UUID> list) {
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

	public Arraylist<Card> extractAll (List<UUID> list) {
		return new Arraylist<>(list.stream().map(this::extractCard).collect(Collectors.toList()));
	}

	public Arraylist<Card> extractAllCopiesFrom (String username, String cardName, Zone zone) {
		Arraylist<Card> cards = new Arraylist<>(getZone(username, zone).parallelStream()
				.filter(c -> c.name.equals(cardName)).collect(Collectors.toList()));
		getZone(username, zone).removeAll(cards);
		return cards;
	}


	/**
	 * Game Action Methods
	 * These are game actions taken by a client
	 */
	public void playCard (String username, UUID cardID) {
		extractCard(cardID).play();
		activePlayer = getOpponentName(username);
	}

	public void activateCard (String username, UUID cardID) {
		getCard(cardID).activate();
		activePlayer = getOpponentName(username);
	}

	public void studyCard (String username, UUID cardID, boolean regular) {
		Card c = extractCard(cardID);
		c.study(getPlayer(username), regular);
		addEvent(Event.study(username, c), regular);
	}

	public void pass (String username) {
		passCount++;
		if (passCount == 2) {
			if (!stack.isEmpty()) {
				resolveStack();
			} else {
				changePhase();
			}
		} else {
			activePlayer = getOpponentName(username);
		}
	}

	public void redraw (String username) {
		getPlayer(username).redraw();
	}

	public void keep (String username) {
		passCount++;
		if (passCount == 2) {
			changePhase();
		} else {
			activePlayer = getOpponentName(username);
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
				activePlayer = " ";
				phase = ATTACK;
				chooseAttackers();
				activePlayer = turnPlayer;
				if (attackers.isEmpty()) {
					phase = MAIN_AFTER;
				}
				break;
			case ATTACK:
				activePlayer = " ";
				phase = BLOCK;
				chooseBlockers();
				activePlayer = getOpponentName(turnPlayer);
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
		if (!endProcessed) {
			endProcessed = true;
			processEndEvents();
			//TODO: figure out logic here
		}
		players.values().forEach(Player::endTurn);
		if (getPlayer(turnPlayer).getHandSize() > 7) {
			discard(turnPlayer, getPlayer(turnPlayer).hand.size() - 7);

		}
		processCleanupEvents();
	}

	private void newTurn () {
		if (turnCount > 0) {
			turnPlayer = getOpponentName(turnPlayer);
			getPlayer(turnPlayer).draw(1);
		}
		activePlayer = turnPlayer;
		passCount = 0;
		getPlayer(turnPlayer).draw(1);
		getPlayer(turnPlayer).newTurn();
		turnCount++;
		processBeginEvents();
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
				triggeringCards.get(getOpponentName(turnPlayer)).forEachInOrder(c -> c.checkEvent(e));
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

	private void processCleanupEvents () {
		//out.println("Starting Cleanup Triggers");
		addEvent(Event.cleanup(turnPlayer), true);
		//out.println("Ending Cleanup Triggers");
	}

	public void addTriggeringCard (String username, Card t) {
		triggeringCards.get(username).add(t);
	}

	public void removeTriggeringCard (Card card) {
		triggeringCards.values().forEach(l -> l.remove(card));
	}

	// Identifier accessor methods
	public String getOpponentName (String playerName) {
		for (String name : players.keySet()) {
			if (!name.equals(playerName)) {
				return name;
			}
		}
		return null;
	}

	public UUID getOpponentId (String username) {
		return getPlayer(getOpponentName(username)).id;
	}


	/**
	 * Stack Methods
	 */
	public void addToStack (Card c) {
		passCount = 0;
		stack.add(0, c);
	}

	// TODO: switch prevSize check to flag system
	private void resolveStack () {
		activePlayer = " ";
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
	 */
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


	/**
	 * Client Prompt Methods
	 * When you need client to do something.
	 */
	//// Helpers
	private void send (String username, ServerGameMessage.Builder builder) {
		try {
			setLastMessage(username, builder.build());
			GameEndpoint e = connections.get(username);
			if (e != null) {
				e.send(builder);
			}
		} catch (IOException | EncodeException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
	}

	private Arraylist<UUID> selectFrom (String username, SelectFromType type, Arraylist<Card> candidates, Arraylist<UUID> canSelect, Arraylist<UUID> canSelectPlayers, int count, boolean upTo, String message) {
		SelectFrom.Builder b = SelectFrom.newBuilder()
				.addAllSelectable(canSelect.transformToStringList())
				.addAllSelectable(canSelectPlayers.transformToStringList())
				.addAllCandidates(candidates.transform(c -> c.toCardMessage().build()))
				.setMessageType(type)
				.setSelectionCount(count)
				.setUpTo(upTo)
				.setMessage(message)
				.setGame(toGameState(username));
		try {
			send(username, ServerGameMessage.newBuilder().setSelectFrom(b));
			String[] l = (String[]) response.take();
			return toUUIDList(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	private Arraylist<AttackerAssignment> selectAttackers (String username) {

		List<String> attackers = getZone(username, Zone.Play).parallelStream()
				.filter(Card::canAttack)
				.map(u -> u.id.toString()).collect(Collectors.toList());

		if (attackers.isEmpty()) {
			return (new Arraylist<>());
		}
		Arraylist<String> targets = new Arraylist<>(getOpponentId(username).toString());
		List<String> allies = getZone(getOpponentName(username), Zone.Play).parallelStream()
				.filter(Predicates::isAlly)
				.map(u -> u.id.toString()).collect(Collectors.toList());
		targets.addAll(allies);
		List<Attacker> attackerList = attackers.parallelStream()
				.map(a -> Attacker.newBuilder()
						.setAttackerId(a)
						.addAllPossibleAttackTargets(targets).build())
				.collect(Collectors.toList());
		out.println("Sending Select Attackers Message to " + username);
		SelectAttackers.Builder b = SelectAttackers.newBuilder()
				.addAllPossibleAttackers(attackerList)
				.setGame(toGameState(username));
		try {
			send(username, ServerGameMessage.newBuilder().setSelectAttackers(b));
			AttackerAssignment[] l = (AttackerAssignment[]) response.take();
			return new Arraylist<>(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	private Arraylist<BlockerAssignment> selectBlockers (String username) {

		List<Card> potentialBlockers =
				getZone(username, Zone.Play)
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

		out.println("Sending Select Blockers Message to " + username);
		SelectBlockers.Builder b = SelectBlockers.newBuilder()
				.addAllPossibleBlockers(blockers)
				.setGame(toGameState(username));
		try {
			send(username, ServerGameMessage.newBuilder().setSelectBlockers(b));
			BlockerAssignment[] l = (BlockerAssignment[]) response.take();
			return new Arraylist<>(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	private Arraylist<DamageAssignment> assignDamage (String username, UUID id, Arraylist<UUID> possibleTargets, int damage) {
		out.println("Sending Assign Damage Message to " + username);
		AssignDamage.Builder b = AssignDamage.newBuilder()
				.setDamageSource(id.toString())
				.addAllPossibleTargets(possibleTargets.transformToStringList())
				.setTotalDamage(damage)
				.setGame(toGameState(username));
		try {
			send(username, ServerGameMessage.newBuilder().setAssignDamage(b));
			DamageAssignment[] l = (DamageAssignment[]) response.take();
			return new Arraylist<>(l);
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return null;
	}

	//// Prompters
	public Arraylist<UUID> selectFromZone (String username, Zone zone, Predicate<Card> validTarget, int count, boolean upTo, String message) {
		Arraylist<UUID> canSelect = new Arraylist<>(getZone(username, zone).parallelStream()
				.filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
		return selectFrom(username, getZoneLabel(zone), getZone(username, zone), canSelect, new Arraylist<>(), count, upTo, message);
	}

	public Arraylist<UUID> selectFromZoneWithPlayers (String username, Zone zone, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo, String message) {
		Arraylist<UUID> canSelect = new Arraylist<>(getZone(username, zone).parallelStream()
				.filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
		Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
				.filter(validPlayer).map(c -> c.id).collect(Collectors.toList()));
		return selectFrom(username, getZoneLabel(zone), getZone(username, zone), canSelect, canSelectPlayers, count, upTo, message);
	}

	public Arraylist<UUID> selectFromList (String username, Arraylist<Card> candidates, Predicate<Card> validTarget, int count, boolean upTo, String message) {
		Arraylist<UUID> canSelect = new Arraylist<>(candidates.parallelStream()
				.filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
		return selectFrom(username, LIST, candidates, canSelect, new Arraylist<>(), count, upTo, message);
	}

	public Arraylist<UUID> selectPlayers (String username, Predicate<Player> validPlayer, int count, boolean upTo) {
		Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
				.filter(validPlayer).map(c -> c.id).collect(Collectors.toList()));
		String message = "Select " + (upTo ? " up to " : "") + count + " player" + (count > 1 ? "s." : ".");
		return selectFrom(username, getZoneLabel(Zone.Play), new Arraylist<>(), new Arraylist<>(), canSelectPlayers, count, upTo, message);
	}

	public Arraylist<UUID> selectDamageTargetsConditional (String username, Predicate<Card> validTarget, Predicate<Player> validPlayer, int count, boolean upTo, String message) {
		return selectFromZoneWithPlayers(username, Both_Play, validTarget, validPlayer, count, upTo, message);
	}

	public Arraylist<UUID> selectDamageTargets (String username, int count, boolean upTo, String message) {
		return selectFromZoneWithPlayers(username, Both_Play, Predicates::isDamageable, Predicates::any, count, upTo, message);
	}

	public void assignDamage (UUID id, Arraylist<UUID> possibleTargets, Damage damage) {
		out.println("Updating players from assignDamage. AP: " + activePlayer);
		updatePlayers();
		Arraylist<DamageAssignment> assignedDamages = assignDamage(turnPlayer, id, possibleTargets, damage.amount);
		out.println("Damage distribution: " + assignedDamages);
		assignedDamages.forEach(c -> {
			UUID targetId = UUID.fromString(c.getTargetId());
			int assignedDamage = c.getDamage();
			dealDamage(id, targetId, new Damage(assignedDamage, damage.mayKill));
		});
	}

	public int selectX (String username, int maxX) {
		if (maxX == 0) {
			return maxX;
		}
		SelectXValue.Builder b = SelectXValue.newBuilder()
				.setMaxXValue(maxX)
				.setGame(toGameState(username));
		try {
			send(username, ServerGameMessage.newBuilder().setSelectXValue(b));

			return (int) response.take();
		} catch (InterruptedException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
		return 0;
	}


	// Unsorted methods
	private Arraylist<Card> getZone (String username, Zone zone) {
		switch (zone) {
			case Deck:
				return getPlayer(username).deck;
			case Hand:
				return getPlayer(username).hand;
			case Play:
				return getPlayer(username).playArea;
			case Discard_Pile:
				return getPlayer(username).discardPile;
			case Void:
				return getPlayer(username).voidPile;
			case Stack:
				return stack;
			case Opponent_Play:
				return getPlayer(getOpponentName(username)).playArea;
			case Opponent_Hand:
				return getPlayer(getOpponentName(username)).hand;
			case Both_Play:
				Arraylist<Card> total = new Arraylist<>();
				players.values().forEach(player -> total.addAll(player.playArea));
				return total;
			default:
				return null;
		}
	}

	public boolean hasIn (String username, Zone zone, Predicate<Card> validTarget, int count) {
		return getZone(username, zone).parallelStream().filter(validTarget).count() >= count;
	}

	public void putTo (String username, Card c, Zone zone) {
		getZone(username, zone).add(c);
	}

	public void putTo (String username, Card c, Zone zone, int index) {
		getZone(username, zone).add(index, c);
	}

	public void putTo (String username, Arraylist<Card> cards, Zone zone) {
		getZone(username, zone).addAll(cards);
	}

	public void addEnergy (String username, int i) {
		getPlayer(username).energy += i;
	}

	public void spendEnergy (String username, int i) {
		getPlayer(username).energy -= i;
	}

	public void draw (String username, int count) {
		Player player = getPlayer(username);
		player.draw(count);
		if (player.deck.isEmpty()) {
			gameEnd(username, false);
		}
	}

	public void draw (String username, UUID cardID) {
		getPlayer(username).hand.add(extractCard(cardID));
	}

	public void draw (String username, Card card) {
		getPlayer(username).hand.add(card);
	}

	public void purge (String username, UUID cardID) {
		getPlayer(username).voidPile.add(extractCard(cardID));
	}

	public void destroy (UUID targetId) {
		destroy(null, targetId);
	}

	public void destroy (UUID sourceId, UUID targetId) {
		Card c = getCard(targetId);
		if (sourceId != null) {
			addEvent(Event.destroy(getCard(sourceId), c));
		}
		c.destroy();
	}

	public void loot (String username, int x) {
		Game.this.draw(username, x);
		discard(username, x);
	}

	public void discard (String username, int count) {
		String message = "Discard " + (count > 1 ? count + " cards." : "a card.");
		Arraylist<Card> d = getPlayer(username).discardAll(selectFromZone(username, Zone.Hand, Predicates::any, count, false, message));
		d.forEach(c -> addEvent(Event.discard(username, c)));
	}

	public void discard (String username, UUID cardID) {
		getPlayer(username).discard(cardID);
		addEvent(Event.discard(username, getCard(cardID)));
	}

	public void discardAll (String username, Arraylist<Card> cards) {
		getPlayer(username).discardAll(UUIDHelper.toUUIDList(cards));
		cards.forEach(c -> addEvent(Event.discard(username, c)));
	}

	public void deplete (UUID id) {
		getCard(id).deplete();
	}

	public void ready (UUID id) {
		getCard(id).newTurn();
	}

	public boolean ownedByOpponent (UUID targetID) {
		Card c = getCard(targetID);
		return c.owner.equals(getOpponentName(c.controller));
	}

	public void payLife (String username, int count) {
		getPlayer(username).payLife(count);
	}

	public void possessTo (String newController, UUID cardID, Zone zone) {
		Card c = extractCard(cardID);
		String oldController = c.controller;
		c.controller = newController;
		getZone(newController, zone).add(c);
		addEvent(possess(oldController, newController, c));
	}

	public boolean controlsUnownedCard (String username, Zone zone) {
		return getZone(username, zone).parallelStream().anyMatch(c -> ownedByOpponent(c.id));
	}

	public boolean isIn (String username, Zone zone, UUID cardID) {
		return getZone(username, zone).parallelStream().anyMatch(getCard(cardID)::equals);
	}


	public int getMaxEnergy (String username) {
		return getPlayer(username).maxEnergy;
	}

	public void addStudyCount (String username, int count) {
		getPlayer(username).numOfStudiesLeft += count;
	}

	public void sacrifice (UUID cardId) {
		getCard(cardId).sacrifice();
	}

	public void gameEnd (String player, boolean win) {
		send(player, ServerGameMessage.newBuilder()
				.setGameEnd(GameEnd.newBuilder()
						.setGame(toGameState(player))
						.setWin(win)));
		send(getOpponentName(player), ServerGameMessage.newBuilder()
				.setGameEnd(GameEnd.newBuilder()
						.setGame(toGameState(getOpponentName(player)))
						.setWin(!win)));
		connections.forEach((s, c) -> c.close());
		connections = new Hashmap<>();
		gameServer.removeGame(id);
	}

	public GameState.Builder toGameState (String username) {
		GameState.Builder b =
				GameState.newBuilder()
						.setId(id.toString())
						.setPlayer(getPlayer(username).toPlayerMessage(true))
						.setOpponent(getPlayer(getOpponentName(username)).toPlayerMessage(false))
						.setTurnPlayer(turnPlayer)
						.setActivePlayer(activePlayer)
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


		players.forEach((s, p) -> {
			if (username.equals(s) && isPlayerActive(s)) {
				p.hand.forEach(c -> {
					if (c.canPlay()) {
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


	public final void updatePlayers () {
		players.forEach((name, player) -> send(name, ServerGameMessage.newBuilder()
				.setUpdateGameState(UpdateGameState.newBuilder()
						.setGame(toGameState(name)))));
	}

	public boolean hasEnergy (String username, int i) {
		return getPlayer(username).energy >= i;
	}

	public boolean hasKnowledge (String username, CounterMap<Knowledge> knowledge) {
		return getPlayer(username).hasKnowledge(knowledge);
	}

	public boolean canPlaySlow (String username) {
		return turnPlayer.equals(username)
				&& activePlayer.equals(username)
				&& stack.isEmpty()
				&& (phase == MAIN_BEFORE || phase == MAIN_AFTER);
	}

	public UUID getId () {
		return id;
	}

	public boolean canStudy (String username) {
		return canPlaySlow(username)
				&& getPlayer(username).numOfStudiesLeft > 0;
	}

	public void shuffleIntoDeck (String username, Card... cards) {
		getPlayer(username).shuffleIntoDeck(cards);
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
				return SelectFromType.DISCARD_PILE;
			case Void:
				return SelectFromType.VOID;
			case Stack:
				return SelectFromType.STACK;
			default:
				return NOTYPE;
		}
	}


	public boolean isPlayerActive (String username) {
		return activePlayer.equals(username);
	}

	public boolean isPlayerInGame (String username) {
		return players.getOrDefault(username, null) != null;
	}


	// Player property getters
	private Player getPlayer (String username) {
		return players.get(username);
	}

	public int getPlayerEnergy (String controller) {
		return getPlayer(controller).energy;
	}

	public String getUsername (UUID playerId) {
		for (int i = 0; i < players.size(); i++) {
			Player p = players.values().toArray(new Player[0])[i];
			if (p.id.equals(playerId)) {
				return p.username;
			}
		}
		return "";
	}

	public UUID getUserId (String username) {
		return getPlayer(username).id;
	}

	// Does nothing if called with non-positive amount of damage
	public void dealDamage (UUID sourceId, UUID targetId, Damage damage) {
		if (damage.amount > 0) {
			String username = getUsername(targetId);
			Card source = getCard(sourceId);
			if (!username.isEmpty()) {
				getPlayer(username).receiveDamage(damage.amount, source);
			} else {
				Card c = getCard(targetId);
				if (c != null) {
					c.receiveDamage(damage, source);

				}
			}
		}
	}

	public boolean isTurnPlayer (String username) {
		return turnPlayer.equals(username);
	}

	public boolean hasMaxEnergy (String username, int count) {
		return getPlayer(username).maxEnergy >= count;
	}

	public void removeMaxEnergy (String username, int count) {
		getPlayer(username).maxEnergy -= count;
	}

	public boolean isPlayer (UUID targetId) {
		return players.values().stream().anyMatch(p -> p.id.equals(targetId));
	}

	public void gainHealth (String username, int health) {
		getPlayer(username).gainHealth(health);
	}

	public void gainHealth (UUID cardId, int health) {
		getCard(cardId).gainHealth(health);
	}

	public void dealDamageToAll (String username, UUID sourceId, Damage damage) {
		players.values().forEach(p -> dealDamage(sourceId, p.id, damage));
		getZone(username, Both_Play).forEach(c -> dealDamage(sourceId, c.id, damage));
	}

	public void donate (UUID donatedCardId, String newController, Zone newZone) {
		Card c = extractCard(donatedCardId);
		String oldController = c.controller;
		c.controller = newController;
		getZone(newController, newZone).add(c);
		addEvent(Event.donate(oldController, newController, c));
	}

	/**
	 * Combat Helper Methods
	 */
	private void chooseAttackers () {
		out.println("Updating players from chooseAttackers. AP: " + activePlayer);
		updatePlayers();
		Arraylist<AttackerAssignment> attackerIds = selectAttackers(turnPlayer);
		out.println("Attackers: " + attackerIds);
		attackerIds.forEach(c -> {
			Card u = getCard(UUID.fromString(c.getAttackerId()));
			u.setAttacking(UUID.fromString(c.getAttacksTo()));
			attackers.add(u.id);
		});
	}

	private void chooseBlockers () {
		out.println("Updating players from chooseBlockers. AP: " + activePlayer);
		updatePlayers();
		Arraylist<BlockerAssignment> assignedBlockers = selectBlockers(getOpponentName(turnPlayer));
		out.println("Blockers: " + assignedBlockers);
		assignedBlockers.forEach(c -> {
			UUID blockerId = UUID.fromString(c.getBlockerId());
			UUID blockedBy = UUID.fromString(c.getBlockedBy());

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

		ArrayList<Card> normalAttackers = new Arraylist<>(getAll(attackers));
		normalAttackers.removeIf(a -> a.hasCombatAbility(FirstStrike));
		ArrayList<Card> normalBlockers = new Arraylist<>(getAll(blockers));

		normalAttackers.forEach(a -> a.dealAttackDamage(false));
		normalBlockers.forEach(Card::dealBlockDamage);

		normalAttackers.forEach(Card::maybeDieFromBlock);
		normalBlockers.forEach(Card::maybeDieFromAttack);
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
		forEachInZone("", Both_Play, Card::returnToHand);
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

	public void putToBottomOfDeck (String username, Arraylist<Card> toBottom) {
		Player player = getPlayer(username);
		toBottom.forEach(player::putToBottomOfDeck);
	}

	public Arraylist<Card> extractFromTopOfDeck (String username, int count) {
		return getPlayer(username).extractFromTopOfDeck(count);
	}

	public void forEachInZone (String username, Zone zone, Consumer<Card> cardConsumer) {
		getZone(username, zone).forEach(cardConsumer);
	}

	public int countInZone (String username, Zone zone, Predicate<Card> cardConsumer) {
		Arraylist<Integer> count = new Arraylist<>();
		getZone(username, zone).forEach(card -> {
			if (cardConsumer.test(card)) {
				count.add(0);
			}
		});
		return count.size();
	}

	public void shuffleDeck (String username) {
		getPlayer(username).shuffleDeck();
	}

	public Card extractTopmostMatchingFromDeck (String username, Predicate<Card> cardPredicate) {
		return getPlayer(username).extractTopmostMatchingFromDeck(cardPredicate);
	}

	public enum Zone {
		Deck, Hand, Opponent_Hand, Play, Opponent_Play, Both_Play, Discard_Pile, Void, Stack
	}

}
