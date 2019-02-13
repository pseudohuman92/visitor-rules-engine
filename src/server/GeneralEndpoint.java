package server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import game.Deck;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import javax.websocket.*;
import javax.websocket.server.*;
import static server.Main.server;
/**
 *
 * @author pseudo
 */

@ServerEndpoint(value="/chat/{username}")
public class GeneralEndpoint {
  
    Session session;
    String username;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        this.username = username;
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        server.addNewConnection(username, session.getId());
    }
 
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {
        handleRequest(message);
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        server.removeConnection(session.getId());
        session = null;
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
 
    public void send(Message message) throws IOException, EncodeException {
        session.getBasicRemote().sendText(message);
    }
    
    public void handleRequest(Message message){
        System.out.println(message);
        switch(message.label){
            case REGISTER:
                String username = (String)message.object;
                if (registerUser(username)){
                    connection.send(Message.success());
                } else {
                    connection.send(Message.fail("Username already exists."));
                }
                break;
            case LOGIN:
                username = (String)message.object;
                if (server.isLoggedIn(username)) {
                    connection.send(Message.fail(username + " is already logged in."));
                } else if (true) { //comment in to enable registering if (userRegistered(username)){
                    server.addLogin(username, connection);
                    connection.send(Message.success());
                } else {
                    connection.send(Message.fail(username + " is not registered."));
                }
                server.updatePlayers();
                break;
            case LOGOUT:
                username = (String)message.object;
                server.removeLogin(username);
                server.updatePlayers();
                server.updateTables();
                break;
            case CHAT_MESSAGE:
                server.appendToChatLog((String)message.object);
                server.updateChatLogs();
                break;
            case CREATE_TABLE:
                Serializable[] data = (Serializable[])message.object;
                server.createTable((String)data[0], (Deck)data[1]);
                server.updateTables();
                break;
            case JOIN_TABLE:
                data = (Serializable[])message.object;
                server.joinTable((String)data[0], (Deck)data[1], (UUID)data[2]);
                server.updateTables();
                break;
            case UPDATE_LOBBY:
                server.updateTables();
                server.updateChatLogs();
                server.updatePlayers();
                break;
            default:
                System.out.println("Unhandled label: "+ message.toString());
        }
    }
}
