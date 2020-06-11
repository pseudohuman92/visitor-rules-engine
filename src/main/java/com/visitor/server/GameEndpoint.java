package com.visitor.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.visitor.protocol.ClientGameMessages;
import com.visitor.protocol.ClientGameMessages.*;
import com.visitor.protocol.ClientGameMessages.ClientGameMessage.PayloadCase;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.AttackerAssignment;
import com.visitor.protocol.Types.BlockerAssignment;
import com.visitor.protocol.Types.SelectFromType;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static com.visitor.protocol.ClientGameMessages.ClientGameMessage.PayloadCase.*;
import static com.visitor.server.GeneralEndpoint.gameServer;
import static java.lang.System.out;
import static java.util.UUID.fromString;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 * @author pseudo
 */
@ServerEndpoint(value = "/games/{gameID}/{username}")
public class GameEndpoint {

	Session session;
	String username;
	UUID gameID;
	boolean waitingResponse;
	PayloadCase responseType;
	SelectFromType selectFromType;
	BufferedWriter writer;


	@OnOpen
	public void onOpen (Session session, @PathParam("gameID") String gameID, @PathParam("username") String username) throws IOException, EncodeException {
		this.session = session;
		this.username = username;
		this.gameID = UUID.fromString(gameID);
		session.getBasicRemote().setBatchingAllowed(false);
		session.getAsyncRemote().setBatchingAllowed(false);
		session.setMaxIdleTimeout(0);
		gameServer.addGameConnection(this.gameID, username, this);
		resendLastMessage();
	}

	@OnMessage
	public void onMessage (Session session, byte[] message) {
		new Thread(() -> {
			try {
				ClientGameMessage cgm = ClientGameMessage.parseFrom(message);
				//writeToLog(cgm);
				//out.println("Received Message");
				//out.println(cgm);
				if (waitingResponse) {
					processResponse(cgm);
				} else {
					processMessage(cgm);
				}
			} catch (InvalidProtocolBufferException ex) {
				getLogger(GameEndpoint.class.getName()).log(SEVERE, null, ex);
			}
		}).start();
	}

	@OnClose
	public void onClose (Session session) {
		gameServer.removeGameConnection(gameID, username);
	}

	@OnError
	public void onError (Session session, Throwable throwable) {
		out.println("[ERROR: " + username + "] " + throwable.getMessage());
		throwable.printStackTrace();
	}

	public void send (ServerGameMessage.Builder builder) throws IOException, EncodeException {
		ServerGameMessage message = builder.build();
		//writeToLog(message);
		checkResponseType(message);
		//out.println("Sent Message");
		//out.println(message);
		session.getBasicRemote().sendObject(message.toByteArray());
	}

	public void close () {
		session = null;
	}

	public void resendLastMessage () throws IOException, EncodeException {
		ServerGameMessage lastMessage = gameServer.getLastMessage(gameID, username);
		if (lastMessage != null) {
			//out.println("Resending last message to "+username+" "+lastMessage);
			checkResponseType(lastMessage);
			session.getBasicRemote().sendObject(lastMessage.toByteArray());
		}
	}

	private void processResponse (ClientGameMessage message) {
		if (message.getPayloadCase() == CONCEDE) {
			gameServer.concede(gameID, username);
			return;
		}
		if (message.getPayloadCase() == SAVEGAMESTATE) {
			gameServer.saveGameState(gameID, message.getSaveGameState().getFilename());
			return;
		}
		if (message.getPayloadCase() == responseType) {
			switch (message.getPayloadCase()) {
				case ORDERCARDSRESPONSE:
					OrderCardsResponse ocr = message.getOrderCardsResponse();
					waitingResponse = false;
					gameServer.addToResponseQueue(gameID, ocr.getOrderedCardsList().toArray(new String[ocr.getOrderedCardsCount()]));
					break;
				case SELECTFROMRESPONSE:
					SelectFromResponse sfr = message.getSelectFromResponse();
					if (sfr.getMessageType() != selectFromType) {
						out.println("Wrong SelectFrom response received from " + username +
								"\nExpected " + selectFromType + " Received: " + message);
						break;
					}
					waitingResponse = false;
					gameServer.addToResponseQueue(gameID, sfr.getSelectedList().toArray(new String[sfr.getSelectedCount()]));
					break;
				case SELECTXVALUERESPONSE:
					waitingResponse = false;
					gameServer.addToResponseQueue(gameID, message.getSelectXValueResponse().getSelectedXValue());
					break;
				case SELECTATTACKERSRESPONSE:
					SelectAttackersResponse sar = message.getSelectAttackersResponse();
					waitingResponse = false;
					gameServer.addToResponseQueue(gameID, sar.getAttackersList().toArray(new AttackerAssignment[sar.getAttackersCount()]));
					break;
				case SELECTBLOCKERSRESPONSE:
					SelectBlockersResponse sbr = message.getSelectBlockersResponse();
					waitingResponse = false;
					gameServer.addToResponseQueue(gameID, sbr.getBlockersList().toArray(new BlockerAssignment[sbr.getBlockersCount()]));
					break;
				case ASSIGNDAMAGERESPONSE:
					ClientGameMessages.AssignDamageResponse ddr = message.getAssignDamageResponse();
					waitingResponse = false;
					gameServer.addToResponseQueue(gameID, ddr.getDamageAssignmentsList().toArray(new Types.DamageAssignment[ddr.getDamageAssignmentsCount()]));
					break;
				default:
					out.println("Wrong Message received from " + username
							+ "\nExpected " + responseType + " Received: " + message);
					break;
			}
			waitingResponse = false;
		} else {
			out.println("Unexpected response from " + username + ": " + message);
		}
	}

	private void processMessage (ClientGameMessage message) {
		switch (message.getPayloadCase()) {
			case PLAYCARD:
				gameServer.playCard(gameID, username, fromString(message.getPlayCard().getCardID()));
				break;
			case ACTIVATECARD:
				gameServer.activateCard(gameID, username, fromString(message.getActivateCard().getCardID()));
				break;
			case STUDYCARD:
				gameServer.studyCard(gameID, username, fromString(message.getStudyCard().getCardID()));
				break;
			case PASS:
				gameServer.pass(gameID, username);
				break;
			case REDRAW:
				gameServer.redraw(gameID, username);
				break;
			case KEEP:
				gameServer.keep(gameID, username);
				break;
			case CONCEDE:
				gameServer.concede(gameID, username);
				break;
			case SAVEGAMESTATE:
				gameServer.saveGameState(gameID, message.getSaveGameState().getFilename());
				break;
			default:
				out.println("Unexpected message from " + username + ": " + message);
				break;
		}
	}

	private void checkResponseType (ServerGameMessage message) {
		switch (message.getPayloadCase()) {
			case ORDERCARDS:
				waitingResponse = true;
				responseType = ORDERCARDSRESPONSE;
				break;
			case SELECTFROM:
				waitingResponse = true;
				responseType = SELECTFROMRESPONSE;
				selectFromType = message.getSelectFrom().getMessageType();
				break;
			case SELECTXVALUE:
				waitingResponse = true;
				responseType = SELECTXVALUERESPONSE;
				break;
			case SELECTATTACKERS:
				waitingResponse = true;
				responseType = SELECTATTACKERSRESPONSE;
				break;
			case SELECTBLOCKERS:
				waitingResponse = true;
				responseType = SELECTBLOCKERSRESPONSE;
				break;
			case ASSIGNDAMAGE:
				waitingResponse = true;
				responseType = ASSIGNDAMAGERESPONSE;
				break;
			default:
				waitingResponse = false;
				break;
		}
	}

	private void writeToLog (ClientGameMessage cgm) {
		try {
			new File("../game-logs/").mkdirs();
			writer = new BufferedWriter(new FileWriter("../game-logs/" + gameID.toString() + ".log", true));
			writer.append("[FROM: ").append(username).append("] ").append(String.valueOf(cgm));
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException ex) {
			getLogger(GameEndpoint.class.getName()).log(SEVERE, null, ex);
		}
	}

	private void writeToLog (ServerGameMessage message) {
		try {
			new File("../game-logs/").mkdirs();
			writer = new BufferedWriter(new FileWriter("../game-logs/" + gameID.toString() + ".log", true));
			writer.append("[TO: ").append(username).append("] ").append(String.valueOf(message));
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException ex) {
			getLogger(GameEndpoint.class.getName()).log(SEVERE, null, ex);
		}
	}
}
