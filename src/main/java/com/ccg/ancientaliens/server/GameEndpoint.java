package com.ccg.ancientaliens.server;

import java.io.IOException;
import java.util.UUID;
import javax.websocket.*;
import javax.websocket.server.*;
/**
 *
 * @author pseudo
 */
@ServerEndpoint(value="/chat/{username}/{gameID}")
public class GameEndpoint {

    Session session;
    String username;
    UUID gameID;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("gameID") String gameID) throws IOException {
        this.session = session;
        this.username = username;
        this.gameID = UUID.fromString(gameID);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        //gameServer.addNewGameConnection(gameID, username, session.getId());
    }
 
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        //handleRequest(message);
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        //gameServer.removeConnection(session.getId());
        session = null;
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
 
    public void send(Message message) throws IOException, EncodeException {
        //session.getBasicRemote().sendText(message);
    }
    /*
    @Override
	public void handleRequest(Message message){
            System.out.println(message);
            Serializable[] data;
            switch(message.label){
                case REGISTER_GAME_CONNECTION:
                    data = (Serializable[])message.object;
                    gameServer.addGameConnection((UUID)data[0], (String)data[1], connection);
                    break;
                case REGISTER_INTERACTION_CONNECTION:
                    data = (Serializable[])message.object;
                    gameServer.addInteractionConnection((UUID)data[0], (String)data[1], connection);
                    stop = true;
                    break;
                case MULLIGAN:
                    data = (Serializable[])message.object;
                    gameServer.mulligan((UUID)data[0], (String)data[1]);
                    gameServer.updateGame((UUID)data[0]);
                    break;
                case KEEP:
                    data = (Serializable[])message.object;
                    gameServer.keep((UUID)data[0], (String)data[1]);
                    gameServer.updateGame((UUID)data[0]);
                    break;
                case PLAY:
                    data = (Serializable[])message.object;
                    gameServer.play((UUID)data[0], (String)data[1], (UUID)data[2], (ArrayList<Serializable>)data[3]);
                    gameServer.updateGame((UUID)data[0]);
                    break;
                case STUDY:
                    data = (Serializable[])message.object;
                    gameServer.study((UUID)data[0], (String)data[1], (UUID)data[2], (Hashmap<Knowledge, Integer>)data[3]);
                    gameServer.updateGame((UUID)data[0]);
                    break;
                case ACTIVATE:
                    data = (Serializable[])message.object;
                    gameServer.activate((UUID)data[0], (String)data[1], (UUID)data[2], (ArrayList<Serializable>)data[3]);
                    gameServer.updateGame((UUID)data[0]);
                    break;
                case PASS:
                    gameServer.skipInitiative((UUID)message.object);
                    //server.updateGame((UUID)message.object);
                    break;
                case CONCEDE:
                    data = (Serializable[])message.object;
                    gameServer.concede((UUID)data[0], (String)data[1]);
                    gameServer.updateTables();
                    break;
                default:
                    System.out.println("Unhandled label: "+ message.toString());
            }
        }*/
}