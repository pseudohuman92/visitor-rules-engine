package com.visitor.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.visitor.helpers.Arraylist;
import com.visitor.protocol.ClientMessages.ClientMessage;
import com.visitor.protocol.ClientMessages.JoinQueue;
import com.visitor.protocol.ServerMessages.ServerMessage;
import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/profiles/{userId}")
public class GeneralEndpoint {
  
    public static GameServer gameServer = null;
    private Session session;
    private String userId;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        this.session = session;
        this.userId = userId;
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        session.setMaxIdleTimeout(0);
        if (gameServer == null) {
            gameServer = new GameServer();
        }
        System.out.println(userId + " connected!");
        gameServer.addConnection(userId, this);
    }
 
    @OnMessage
    public void onMessage(Session session, byte[] message) throws IOException {
        ClientMessage cm = ClientMessage.parseFrom(message);
        System.out.println(userId + " sent a message: " + cm);
        handleMessage(cm);
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println(userId + " disconnected!");
        gameServer.removeConnection(userId);
        this.session = null;
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("General " + userId + " ERROR!");
        throwable.printStackTrace();
    }
 
    public void send(ServerMessage message) throws IOException, EncodeException {
        System.out.println("Server sending a message to " + userId + ": " + message);
        session.getBasicRemote().sendObject(message.toByteArray());
    }
    
    public void send(ServerMessage.Builder builder) throws IOException, EncodeException {
        ServerMessage message = builder.build();
        System.out.println("Server sending a message to " + userId + ": " + message);
        session.getBasicRemote().sendObject(message.toByteArray());
    }

    private void handleMessage(ClientMessage cm) {
        switch(cm.getPayloadCase()){
            case JOINQUEUE:
                //Temporary implementation
                JoinQueue jqm = cm.getJoinQueue();
                gameServer.joinQueue(userId, jqm.getDecklistList().toArray(new String[jqm.getDecklistCount()]));
                break;
        }
    }
}
