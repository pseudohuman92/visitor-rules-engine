package com.ccg.ancientaliens.server;

import com.ccg.ancientaliens.protocol.ClientGameMessages.*;
import com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage.PayloadCase;
import static com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage.PayloadCase.*;
import com.ccg.ancientaliens.protocol.ServerGameMessages.*;
import static com.ccg.ancientaliens.server.GeneralEndpoint.gameServer;
import java.io.IOException;
import java.util.UUID;
import javax.websocket.*;
import javax.websocket.server.*;
/**
 *
 * @author pseudo
 */
@ServerEndpoint(value="/games/{gameID}/{username}")
public class GameEndpoint {

    Session session;
    String username;
    UUID gameID;
    boolean waitingResponse;
    PayloadCase responseType;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("gameID") String gameID) throws IOException {
        this.session = session;
        this.username = username;
        this.gameID = UUID.fromString(gameID);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        gameServer.addGameConnection(this.gameID, username, this);
    }
 
    @OnMessage
    public void onMessage(Session session, byte[] message) throws IOException {
        ClientGameMessage cgm = ClientGameMessage.parseFrom(message);
        System.out.println(username + " sent a game message: " + cgm);
        if (waitingResponse){
            processResponse(cgm);
        } else {
            processMessage(cgm);
        }
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        gameServer.removeGameConnection(gameID, username);
        this.session = null;
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println(username + " ERROR!");
    }
 
    public void send(ServerGameMessage message) throws IOException, EncodeException {
        System.out.println("Server sending a game message to " + username + ": " + message);
        checkNeedsResponse(message);     
        session.getBasicRemote().sendObject(message.toByteArray());
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

    private void processResponse(ClientGameMessage message) {
        if (message.getPayloadCase() == responseType){
            switch(message.getPayloadCase()){
                case ORDERCARDSRESPONSE:
                    waitingResponse = false;
                    break;
                case SELECTFROMLISTRESPONSE:
                    waitingResponse = false;
                    break;
                case SELECTFROMPLAYRESPONSE:
                    waitingResponse = false;
                    break;
                case SELECTFROMHANDRESPONSE:
                    waitingResponse = false;
                    break;
                case SELECTFROMSCRAPYARDRESPONSE:
                    waitingResponse = false;
                    break;
                case SELECTFROMVOIDRESPONSE:
                    waitingResponse = false;
                    break;
                case SELECTPLAYERRESPONSE:
                    waitingResponse = false;
                    break;
                default:
                    System.out.println("Expected " + responseType + " from " + username + ". Received: " + message);
                    break;
            }
            waitingResponse = false;
        } else {
            System.out.println("Unexpected response from " + username + ": " + message);
        }
    }

    private void processMessage(ClientGameMessage message) {
        switch(message.getPayloadCase()){
            case PLAYCARD:
                PlayCard payload1 = message.getPlayCard();
                gameServer.playCard(gameID, username, UUID.fromString(payload1.getCardID()));
                break;
            case ACTIVATECARD:
                ActivateCard payload2 = message.getActivateCard();
                gameServer.activateCard(gameID, username, UUID.fromString(payload2.getCardID()));
                break;
            case STUDYCARD:
                StudyCard payload3 = message.getStudyCard();
                gameServer.studyCard(gameID, username, UUID.fromString(payload3.getCardID()), payload3.getKnowledgeList());
                break;
            case PASS:
                gameServer.pass(gameID, username);
                break;
            case MULLIGAN:
                gameServer.mulligan(gameID, username);
                break;
            case KEEP:
                gameServer.keep(gameID, username);
                break;
            case CONCEDE:
                gameServer.concede(gameID, username);
                break;
            default:
                System.out.println("Unexpected response from " + username + ": " + message);
                break;
        }
    }

    private void checkNeedsResponse(ServerGameMessage message) {
        switch(message.getPayloadCase()){
            case ORDERCARDS:
                waitingResponse = true;
                responseType = ORDERCARDSRESPONSE;
                break;
            case SELECTFROMLIST:
                waitingResponse = true;
                responseType = SELECTFROMLISTRESPONSE;
                break;
            case SELECTFROMPLAY:
                waitingResponse = true;
                responseType = SELECTFROMPLAYRESPONSE;
                break;
            case SELECTFROMHAND:
                waitingResponse = true;
                responseType = SELECTFROMHANDRESPONSE;
                break;
            case SELECTFROMSCRAPYARD:
                waitingResponse = true;
                responseType = SELECTFROMSCRAPYARDRESPONSE;
                break;
            case SELECTFROMVOID:
                waitingResponse = true;
                responseType = SELECTFROMVOIDRESPONSE;
                break;
            case SELECTPLAYER:
                waitingResponse = true;
                responseType = SELECTPLAYERRESPONSE;
                break;
            default:
                waitingResponse = false;
                break;
        }
    }
}