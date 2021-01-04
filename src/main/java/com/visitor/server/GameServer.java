package com.visitor.server;

import com.visitor.game.Draft;
import com.visitor.game.Game;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.ServerMessages;
import com.visitor.protocol.ServerMessages.LoginResponse;
import com.visitor.protocol.ServerMessages.NewGame;
import com.visitor.protocol.ServerMessages.ServerMessage;
import com.visitor.protocol.Types;

import javax.websocket.EncodeException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.System.out;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 * @author pseudo
 */
public class GameServer {

	public Hashmap<String, GeneralEndpoint> playerConnections;
	public Hashmap<UUID, Game> games;
	public Hashmap<UUID, Draft> drafts;
	public Arraylist<String> chatLog;
	public Arraylist<QueuePlayer> Bo1ConstructedQueue;
	public Arraylist<QueuePlayer> P2DraftQueue;
	public Hashmap<UUID, QueuePlayer> P2DraftGameQueue;

	public GameServer () {
		playerConnections = new Hashmap<>();
		chatLog = new Arraylist<>();
		games = new Hashmap<>();
		drafts = new Hashmap<>();
		Bo1ConstructedQueue = new Arraylist<>();
		P2DraftQueue = new Arraylist<>();
		P2DraftGameQueue = new Hashmap<>();
	}


	void activateCard (UUID gameID, UUID playerId, UUID cardID) {
		System.out.println("Start Activate Card");
		games.get(gameID).activateCard(playerId, cardID);
		out.println("Updating players from activateCard.");
		games.get(gameID).updatePlayers();
		System.out.println("End Activate Card");
	}

	void concede (UUID gameID, UUID playerId) {
		games.get(gameID).gameEnd(playerId, false);
	}

	void redraw (UUID gameID, UUID playerId) {
		System.out.println("Start Redraw");
		games.get(gameID).redraw(playerId);
		out.println("Updating players from redraw.");
		games.get(gameID).updatePlayers();
		System.out.println("End Redraw");
	}

	void keep (UUID gameID, UUID playerId) {
		System.out.println("Start Keep");
		games.get(gameID).keep(playerId);
		out.println("Updating players from keep.");
		games.get(gameID).updatePlayers();
		System.out.println("End Keep");
	}

	void pass (UUID gameID, UUID playerId) {
		System.out.println("Start Pass");
		games.get(gameID).pass(playerId);
		out.println("Updating players from pass.");
		games.get(gameID).updatePlayers();
		System.out.println("End Pass");
	}

	void studyCard (UUID gameID, UUID playerId, UUID cardID) {
		System.out.println("Start Study Card");
		games.get(gameID).studyCard(playerId, cardID, true);
		out.println("Updating players from studyCard.");
		games.get(gameID).updatePlayers();
		System.out.println("End Study Card");
	}

	void playCard (UUID gameID, UUID playerId, UUID cardID) {
		System.out.println("Start Play Card");
		games.get(gameID).playCard(playerId, cardID);
		out.println("Updating players from playCard.");
		games.get(gameID).updatePlayers();
		System.out.println("End Play Card");
	}

	synchronized ServerGameMessage getLastMessage (UUID gameID, UUID playerId) {
		return games.get(gameID).getLastMessage(playerId);
	}

	synchronized void addConnection (String username, GeneralEndpoint connection) {
		try {
			playerConnections.putIn(username, connection);
			final UUID[] playerGame = {null};
			final UUID[] playerId = {null};
			games.forEach((id, game) -> {
				if (game.isPlayerInGame(username)) {
					playerGame[0] = id;
					playerId[0] = game.getPlayerId(username);
				}
			});
			connection.send(ServerMessage.newBuilder().setLoginResponse(LoginResponse.newBuilder()
					.setGameId(playerGame[0] != null ? playerGame[0].toString() : "")
					.setPlayerId(playerId[0] != null ? playerId[0].toString() : "")));
		} catch (IOException | EncodeException ex) {
			getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
		}
	}

	synchronized void removeConnection (String username) {
		playerConnections.removeFrom(username);
		for (int i = 0; i < Bo1ConstructedQueue.size(); i++) {
			QueuePlayer p = Bo1ConstructedQueue.get(i);
			if (p.username.equals(username)) {
				Bo1ConstructedQueue.remove(p);
				i--;
			}
		}
	}

	synchronized void addGameConnection (UUID gameID, UUID playerId, GameEndpoint connection) {
		games.get(gameID).addConnection(playerId, connection);
	}

	synchronized void removeGameConnection (UUID gameID, UUID playerId) {
		games.get(gameID).removeConnection(playerId);
	}

	synchronized void joinQueue (String username, Types.GameType gameType, String[] decklist, UUID draftId) {
		switch(gameType){
			case BO1_CONSTRUCTED:
				join1Bo1ConstructedQueue(username, decklist);
				break;
			case P2_DRAFT:
				joinP2DraftQueue(username);
				break;
			case P2_DRAFT_GAME:
				joinP2DraftGameQueue(draftId, username, decklist);
				break;
		}

	}

	private void joinP2DraftGameQueue (UUID draftId, String username, String[] decklist) {
		if (!P2DraftGameQueue.containsKey(draftId)) {
			out.println("Adding " + username + " to draft game queue!");
			P2DraftGameQueue.put(draftId, new QueuePlayer(username, decklist));
		} else {
			if (P2DraftGameQueue.get(draftId).username.equals(username)) {
				return;
			}
			QueuePlayer waitingPlayer = P2DraftGameQueue.remove(draftId);
			out.println("Starting a new draft game with " + username + " and " + waitingPlayer.username);
			Game game = new Game();
			game.addPlayers(new Player(game, waitingPlayer.username, waitingPlayer.decklist), new Player(game, username, decklist));
			games.putIn(game.getId(), game);
			try {
				playerConnections.get(waitingPlayer.username).send(ServerMessage.newBuilder()
						.setNewGame(NewGame.newBuilder()
								.setGame(game.toGameState(waitingPlayer.username))));
				playerConnections.get(username).send(ServerMessage.newBuilder()
						.setNewGame(NewGame.newBuilder()
								.setGame(game.toGameState(username))));
			} catch (IOException | EncodeException ex) {
				getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
			}
		}
	}

	private void joinP2DraftQueue (String username) {
		if (P2DraftQueue.isEmpty()) {
			out.println("Adding " + username + " to draft queue!");
			P2DraftQueue.add(new QueuePlayer(username, null));
		} else {
			if (P2DraftQueue.get(0).username.equals(username)) {
				return;
			}
			QueuePlayer waitingPlayer = P2DraftQueue.remove(0);
			out.println("Starting a new draft with " + username + " and " + waitingPlayer.username);
			Draft draft = new Draft(waitingPlayer.username, username);
			drafts.putIn(draft.getId(), draft);
			try {
				playerConnections.get(waitingPlayer.username).send(ServerMessage.newBuilder()
						.setNewDraft(ServerMessages.NewDraft.newBuilder()
								.setDraft(draft.toDraftState(waitingPlayer.username, false))));
				playerConnections.get(username).send(ServerMessage.newBuilder()
						.setNewDraft(ServerMessages.NewDraft.newBuilder()
								.setDraft(draft.toDraftState(username, false))));
			} catch (IOException | EncodeException ex) {
				getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
			}
		}
	}

	private void join1Bo1ConstructedQueue (String username, String[] decklist) {
		if (Bo1ConstructedQueue.isEmpty()) {
			out.println("Adding " + username + " to constructed game queue!");
			Bo1ConstructedQueue.add(new QueuePlayer(username, decklist));
		} else {
			if (Bo1ConstructedQueue.get(0).username.equals(username)) {
				return;
			}
			QueuePlayer waitingPlayer = Bo1ConstructedQueue.remove(0);
			out.println("Starting a new constructed game with " + username + " and " + waitingPlayer.username);
			Game game = new Game();
			game.addPlayers(new Player(game, waitingPlayer.username, waitingPlayer.decklist), new Player(game, username, decklist));
			games.putIn(game.getId(), game);
			try {
				playerConnections.get(waitingPlayer.username).send(ServerMessage.newBuilder()
						.setNewGame(NewGame.newBuilder()
								.setGame(game.toGameState(waitingPlayer.username))));
				playerConnections.get(username).send(ServerMessage.newBuilder()
						.setNewGame(NewGame.newBuilder()
								.setGame(game.toGameState(username))));
			} catch (IOException | EncodeException ex) {
				getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
			}
		}
	}

	void addToResponseQueue (UUID gameID, Object o) {
		games.get(gameID).addToResponseQueue(o);
	}

	public void removeGame (UUID gameID) {
		games.remove(gameID);
	}

	public void saveGameState (UUID gameID, String filename) {
		games.get(gameID).saveGameState(filename);
	}

	public void loadGame (UUID playerId, String filename) {

		try {
			FileInputStream fileIn = new FileInputStream(filename + ".gamestate");
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			Object obj = objectIn.readObject();

			System.out.println("Game state has been read from the file");
			objectIn.close();

			Game g = (Game) obj;
			g.connections = new Hashmap<>();
			g.response = new ArrayBlockingQueue<>(1);
			games.putIn(g.getId(), g);
			try {
				playerConnections.get(playerId).send(ServerMessage.newBuilder().setLoginResponse(LoginResponse.newBuilder()
						.setGameId(g.getId().toString())));
			} catch (IOException | EncodeException ex) {
				getLogger(GameServer.class.getName()).log(SEVERE, null, ex);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	synchronized public void addDraftConnection (UUID draftID, UUID playerId, DraftEndpoint connection) {
		drafts.get(draftID).addConnection(playerId, connection);
	}

	synchronized public void removeDraftConnection (UUID draftID, UUID playerId) {
		drafts.get(draftID).removeConnection(playerId);
	}

	public void draftPick (UUID draftID, UUID playerId, UUID cardId) {
		System.out.println("Start Draft Pick");
		drafts.get(draftID).draftPick(playerId, cardId);
		System.out.println("End Draft Pick");
	}

	public ServerGameMessage getDraftLastMessage (UUID draftID, UUID playerId) {
		return drafts.get(draftID).getLastMessage(playerId);
	}
}
