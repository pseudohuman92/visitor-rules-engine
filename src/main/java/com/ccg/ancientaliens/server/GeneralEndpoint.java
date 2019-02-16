package com.ccg.ancientaliens.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.ccg.ancientaliens.protocol.ClientMessages.ClientMessage;
import com.ccg.ancientaliens.protocol.ServerMessages.ServerMessage;
import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/profiles/{username}")
public class GeneralEndpoint {
  
    static GameServer gameServer = null;
    Session session;
    String username;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        this.username = username;
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        if (gameServer == null) {
            gameServer = new GameServer();
        }
        System.out.println(username + " connected!");
        gameServer.addConnection(username, this);
    }
 
    @OnMessage
    public void onMessage(Session session, byte[] message) throws IOException {
        ClientMessage cm = ClientMessage.parseFrom(message);
        System.out.println(username + " sent a message: " + cm);
        handleMessage(cm);
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println(username + " disconnected!");
        gameServer.removeConnection(username);
        this.session = null;
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println(username + " ERROR!");
    }
 
    public void send(ServerMessage message) throws IOException, EncodeException {
        System.out.println("Server sending a message to " + username + ": " + message);
        session.getBasicRemote().sendObject(message.toByteArray());
    }

    private void handleMessage(ClientMessage cm) {
        switch(cm.getPayloadCase()){
            case JOINTABLE:
                //Temporary implementation
                gameServer.joinTable(username);
                break;
        }
    }
}
