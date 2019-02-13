package server;

import enums.Knowledge;
import helpers.Hashmap;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import javax.websocket.*;
import javax.websocket.server.*;
import network.Message;
import network.Receiver;
import static server.Main.server;

/**
 *
 * @author pseudo
 */
@ServerEndpoint(value="/chat/{username}/{gameID}")
public class GameEndpoint extends Receiver {

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
        server.addNewGameConnection(gameID, username, session.getId());
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
    /**
     *
     * @param message
     */
    @Override
	public void handleRequest(Message message){
            System.out.println(message);
            Serializable[] data;
            switch(message.label){
                case REGISTER_GAME_CONNECTION:
                    data = (Serializable[])message.object;
                    server.addGameConnection((UUID)data[0], (String)data[1], connection);
                    break;
                case REGISTER_INTERACTION_CONNECTION:
                    data = (Serializable[])message.object;
                    server.addInteractionConnection((UUID)data[0], (String)data[1], connection);
                    stop = true;
                    break;
                case MULLIGAN:
                    data = (Serializable[])message.object;
                    server.mulligan((UUID)data[0], (String)data[1]);
                    server.updateGame((UUID)data[0]);
                    break;
                case KEEP:
                    data = (Serializable[])message.object;
                    server.keep((UUID)data[0], (String)data[1]);
                    server.updateGame((UUID)data[0]);
                    break;
                case PLAY:
                    data = (Serializable[])message.object;
                    server.play((UUID)data[0], (String)data[1], (UUID)data[2], (ArrayList<Serializable>)data[3]);
                    server.updateGame((UUID)data[0]);
                    break;
                case STUDY:
                    data = (Serializable[])message.object;
                    server.study((UUID)data[0], (String)data[1], (UUID)data[2], (Hashmap<Knowledge, Integer>)data[3]);
                    server.updateGame((UUID)data[0]);
                    break;
                case ACTIVATE:
                    data = (Serializable[])message.object;
                    server.activate((UUID)data[0], (String)data[1], (UUID)data[2], (ArrayList<Serializable>)data[3]);
                    server.updateGame((UUID)data[0]);
                    break;
                case PASS:
                    server.skipInitiative((UUID)message.object);
                    //server.updateGame((UUID)message.object);
                    break;
                case CONCEDE:
                    data = (Serializable[])message.object;
                    server.concede((UUID)data[0], (String)data[1]);
                    server.updateTables();
                    break;
                default:
                    System.out.println("Unhandled label: "+ message.toString());
            }
        }
}