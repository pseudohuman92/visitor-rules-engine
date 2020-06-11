package com.visitor.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.visitor.protocol.ClientMessages.ClientMessage;
import com.visitor.protocol.ClientMessages.JoinQueue;
import com.visitor.protocol.ServerMessages.ServerMessage;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static java.lang.System.out;

@ServerEndpoint(value = "/profiles/{username}")
public class GeneralEndpoint {

	public static GameServer gameServer = null;
	private Session session;
	private String username;

	@OnOpen
	public void onOpen (Session session, @PathParam("username") String username) throws IOException {
		this.session = session;
		this.username = username;
		session.getBasicRemote().setBatchingAllowed(false);
		session.getAsyncRemote().setBatchingAllowed(false);
		session.setMaxIdleTimeout(0);
		if (gameServer == null) {
			gameServer = new GameServer();
		}
		out.println(username + " connected!");
		gameServer.addConnection(username, this);
	}

	@OnMessage
	public void onMessage (Session session, byte[] message) throws IOException {
		ClientMessage cm = ClientMessage.parseFrom(message);
		out.println(username + " sent a message: " + cm);
		handleMessage(cm);
	}

	@OnClose
	public void onClose (Session session) {
		out.println(username + " disconnected!");
		gameServer.removeConnection(username);
		this.session = null;
	}

	@OnError
	public void onError (Session session, Throwable throwable) {
		out.println("General " + username + " ERROR!");
		throwable.printStackTrace();
	}

	public void send (ServerMessage message) throws IOException, EncodeException {
		out.println("Server sending a message to " + username + ": " + message);
		session.getBasicRemote().sendObject(message.toByteArray());
	}

	public void send (ServerMessage.Builder builder) throws IOException, EncodeException {
		ServerMessage message = builder.build();
		out.println("Server sending a message to " + username + ": " + message);
		session.getBasicRemote().sendObject(message.toByteArray());
	}

	private void handleMessage (ClientMessage cm) {
		switch (cm.getPayloadCase()) {
			case JOINQUEUE:
				//Temporary implementation
				JoinQueue jqm = cm.getJoinQueue();
				gameServer.joinQueue(username, jqm.getDecklistList().toArray(new String[jqm.getDecklistCount()]));
				break;
			case LOADGAME:
				gameServer.loadGame(username, cm.getLoadGame().getFilename());
				break;
		}
	}
}
