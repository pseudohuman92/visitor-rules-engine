package com.visitor.game;

import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.UUIDHelper;
import com.visitor.protocol.ServerGameMessages;
import com.visitor.protocol.Types;
import com.visitor.server.DraftEndpoint;
import com.visitor.sets.base.BladeDervish;
import com.visitor.sets.base.CreepingCanopyVine;
import com.visitor.sets.base.Eagle;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import static java.util.UUID.randomUUID;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class Draft {

	public transient Hashmap<String, DraftEndpoint> connections;
	public transient Hashmap<String, ArrayBlockingQueue<Object>> responses;
	Hashmap<String, Player> players;
	Hashmap<String, ServerGameMessages.ServerGameMessage> lastMessages;
	Hashmap<String, Arraylist<Card>> possiblePicks;
	UUID id;
	int picksReceived;
	int roundPickCount;
	int roundsCompleted;
	boolean started;

	private final int TOTAL_ROUNDS = 3;

	public Draft (String username1, String username2) {
		id = randomUUID();
		players = new Hashmap<>();
		connections = new Hashmap<>();
		lastMessages = new Hashmap<>();
		possiblePicks = new Hashmap<>();
		responses = new Hashmap<>();
		started = false;
		picksReceived = 0;
		roundPickCount = 0;
		roundsCompleted = 0;

		responses.put(username1, new ArrayBlockingQueue<>(1));
		responses.put(username2, new ArrayBlockingQueue<>(1));
		players.put(username1, new Player(null, username1, new String[0]));
		players.put(username2, new Player(null, username2, new String[0]));
	}

	public void startDraft () {
		started = true;
		startDraftRound();
	}

	private void startDraftRound () {
		System.out.println("Starting draft round " + (roundsCompleted + 1));
		possiblePicks.clear();
		players.keySet().forEach(username ->
				possiblePicks.put(username, getRandomCards(username, 3))
		);
		System.out.println("Sending first picks");
		possiblePicks.forEach(this::pick);
	}

	//TODO: implement this
	private Arraylist<Card> getRandomCards (String username, int count) {
		Arraylist<Card> cards = new Arraylist<>();

		cards.add(new BladeDervish(null, username));
		cards.add(new CreepingCanopyVine(null, username));
		cards.add(new Eagle(null, username));

		return cards;
	}

	private void addLastCards () {
		String arbitraryPlayer = possiblePicks.getArbitraryKey();
		players.get(getOpponentName(arbitraryPlayer)).deck.addAll(possiblePicks.get(arbitraryPlayer));
		players.get(arbitraryPlayer).deck.addAll(possiblePicks.get(getOpponentName(arbitraryPlayer)));
	}

	private void swapPossiblePicks () {
		String arbitraryPlayer = possiblePicks.getArbitraryKey();
		Arraylist<Card> tempCards = possiblePicks.get(arbitraryPlayer);
		Hashmap<String, Arraylist<Card>> tempPossiblePicks = new Hashmap<>();

		tempPossiblePicks.put(arbitraryPlayer, possiblePicks.get(getOpponentName(arbitraryPlayer)));
		tempPossiblePicks.put(getOpponentName(arbitraryPlayer), tempCards);
		possiblePicks = tempPossiblePicks;
	}

	public void processPicks () {
		responses.forEach((username, response) -> {
					UUID pickedCardId = null;
					try {
						pickedCardId = (UUID) response.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					//System.out.println("Received Pick\nFrom: " + username + "\nCard ID: " + pickedCardId);

					Card pickedCard = UUIDHelper.getInList(possiblePicks.get(username), new Arraylist<>(pickedCardId)).get(0);
					possiblePicks.get(username).remove(pickedCard);
					players.get(username).deck.putIn(pickedCard);
				});

			roundPickCount++;
			continueDraft();
	}

	private void continueDraft () {
		if (roundPickCount == 1) {
			System.out.println("Swap possible picks");
			swapPossiblePicks();

			System.out.println("Sending second picks");
			possiblePicks.forEach(this::pick);
		} else {
			roundPickCount = 0;
			System.out.println("Adding last cards");
			addLastCards();
			System.out.println("Ending draft round " + (roundsCompleted + 1));

			roundsCompleted++;
			if (roundsCompleted < TOTAL_ROUNDS) {
				startDraftRound();
			} else {
				players.keySet().forEach(this::sendCompleted);
			}
		}
	}

	private void pick (String username, Arraylist<Card> cards) {
		ServerGameMessages.PickCard.Builder builder = ServerGameMessages.PickCard.newBuilder()
				.addAllCandidates(cards.transform(c -> c.toCardMessage().build()))
				.setMessage("Pick a card to draft.").setDraft(toDraftState(username, false));
		send(username, ServerGameMessages.ServerGameMessage.newBuilder().setPickCard(builder));
	}

	private void sendCompleted (String username) {
		ServerGameMessages.PickCard.Builder builder = ServerGameMessages.PickCard.newBuilder()
				.setMessage("Draft Completed.").setDraft(toDraftState(username, true));
		send(username, ServerGameMessages.ServerGameMessage.newBuilder().setPickCard(builder));
	}

	private void send (String username, ServerGameMessages.ServerGameMessage.Builder builder) {
		try {
			setLastMessage(username, builder.build());
			DraftEndpoint e = connections.get(username);
			if (e != null) {
				e.send(builder);
			} else {
				System.out.println(username + " Connection not found!");
			}
		} catch (IOException | EncodeException ex) {
			getLogger(Game.class.getName()).log(SEVERE, null, ex);
		}
	}

	/**
	 * Connection Methods
	 * To deal with client connections
	 */
	public void addConnection (String username, DraftEndpoint connection) {
		connections.putIn(username, connection);
		if (!started && connections.keySet().size() > 1){
			startDraft();
		}
	}

	public void removeConnection (String username) {
		connections.removeFrom(username);
	}

	public void setLastMessage (String username, ServerGameMessages.ServerGameMessage lastMessage) {
		lastMessages.put(username, lastMessage);
	}

	public ServerGameMessages.ServerGameMessage getLastMessage (String username) {
		return lastMessages.get(username);
	}

	public UUID getId () {
		return id;
	}

	public String getOpponentName (String playerName) {
		for (String name : players.keySet()) {
			if (!name.equals(playerName)) {
				return name;
			}
		}
		return null;
	}

	public Types.DraftState.Builder toDraftState (String username, boolean completed) {
		Types.DraftState.Builder b =
				Types.DraftState.newBuilder()
						.setId(id.toString())
						.addAllDecklist(players.get(username).deck.toDeckList())
						.setCompleted(completed);
		return b;
	}

	public void draftPick (String username, UUID cardId) {
		try {
			responses.get(username).put(cardId);
			picksReceived++;
			if (picksReceived == 2){
				picksReceived = 0;
				processPicks();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
