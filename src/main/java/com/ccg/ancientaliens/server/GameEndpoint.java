package com.ccg.ancientaliens.server;

import com.ccg.ancientaliens.protocol.ClientGameMessages.ActivateCard;
import com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage;
import com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage.PayloadCase;
import static com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage.PayloadCase.*;
import com.ccg.ancientaliens.protocol.ClientGameMessages.*;
import com.ccg.ancientaliens.protocol.ClientGameMessages.SelectPlayerResponse;
import com.ccg.ancientaliens.protocol.ClientGameMessages.StudyCard;
import com.ccg.ancientaliens.protocol.ServerGameMessages.ServerGameMessage;
import static com.ccg.ancientaliens.server.GeneralEndpoint.gameServer;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    ArrayBlockingQueue<Object> response = new ArrayBlockingQueue<>(1);
    
    @OnOpen
    public void onOpen(Session session, @PathParam("gameID") String gameID, @PathParam("username") String username) throws IOException {
        this.session = session;
        this.username = username;
        this.gameID = UUID.fromString(gameID);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        gameServer.addGameConnection(this.gameID, username, this);
    }
 
    @OnMessage
    public void onMessage(Session session, byte[] message) throws IOException {
        new Thread (() -> {
            try {
                ClientGameMessage cgm = ClientGameMessage.parseFrom(message);
                System.out.println(username + " sent a game message: " + cgm);
                if (waitingResponse){
                    processResponse(cgm);
                } else {
                    processMessage(cgm);
                }
            } catch (InvalidProtocolBufferException ex) {
                Logger.getLogger(GameEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        gameServer.removeGameConnection(gameID, username);
        this.session = null;
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Game " + username + " ERROR!: " + throwable.getMessage());
        throwable.printStackTrace();
    }
 
    public void send(ServerGameMessage message) throws IOException, EncodeException {
        System.out.println("Server sending a game message to " + username + ": " + message);  
        session.getBasicRemote().sendObject(message.toByteArray());
    }
    
    public void send(ServerGameMessage.Builder builder) throws IOException, EncodeException {
        ServerGameMessage message = builder.build();
        System.out.println("Server sending a game message to " + username + ": " + message);   
        session.getBasicRemote().sendObject(message.toByteArray());
    }
    
    public void sendForResponse(ServerGameMessage.Builder builder) throws IOException, EncodeException {
        ServerGameMessage message = builder.build();
        System.out.println("Server sending a game message to " + username + ": " + message);
        checkResponseType(message);
        session.getBasicRemote().sendObject(message.toByteArray());
    }
    
    public Object getResponse() throws InterruptedException {
        return response.take();
    }

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
                    SelectFromPlayResponse sfpr = message.getSelectFromPlayResponse();
                    waitingResponse = false;
                    response.add(sfpr.getSelectedCardsList().toArray(new String[sfpr.getSelectedCardsCount()]));
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
                    SelectPlayerResponse spr = message.getSelectPlayerResponse();
                    waitingResponse = false;
                    break;
                case SELECTXVALUERESPONSE:
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
                gameServer.studyCard(gameID, username, UUID.fromString(payload3.getCardID()));
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

    private void checkResponseType(ServerGameMessage message) {
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
            case SELECTXVALUE:
                waitingResponse = true;
                responseType = SELECTXVALUERESPONSE;
                break;
            default:
                waitingResponse = false;
                break;
        }
    }
}