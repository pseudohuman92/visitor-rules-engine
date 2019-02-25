package com.ccg.ancientaliens.server;

import com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage;
import static com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage.PayloadCase.*;
import com.ccg.ancientaliens.protocol.ClientGameMessages.*;
import com.ccg.ancientaliens.protocol.ClientGameMessages.ClientGameMessage.PayloadCase;
import com.ccg.ancientaliens.protocol.ServerGameMessages.ServerGameMessage;
import com.ccg.ancientaliens.protocol.Types.SelectFromType;
import static com.ccg.ancientaliens.server.GeneralEndpoint.gameServer;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
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
    SelectFromType selectFromType;
    ArrayBlockingQueue<Object> response = new ArrayBlockingQueue<>(1);
    BufferedWriter writer;

    
    @OnOpen
    public void onOpen(Session session, @PathParam("gameID") String gameID, @PathParam("username") String username) throws IOException {
        this.session = session;
        this.username = username;
        this.gameID = UUID.fromString(gameID);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        session.setMaxIdleTimeout(0);
        gameServer.addGameConnection(this.gameID, username, this);
    }
 
    @OnMessage
    public void onMessage(Session session, byte[] message) throws IOException {
        new Thread (() -> {
            try {
                ClientGameMessage cgm = ClientGameMessage.parseFrom(message);
                if(writer == null) {
                    writer = new BufferedWriter(new FileWriter(gameID.toString()+".log", true));
                }
                writer.append("[FROM: " + username + "] " + cgm);
                writer.flush();
                
                if (waitingResponse){
                    processResponse(cgm);
                } else {
                    processMessage(cgm);
                }
            } catch (InvalidProtocolBufferException ex) {
                Logger.getLogger(GameEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GameEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        gameServer.removeGameConnection(gameID, username);
        writer.close();
        writer = null;
        this.session = null;
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("[ERROR: " + username + "] " + throwable.getMessage());
        throwable.printStackTrace();
    }
    
    public void send(ServerGameMessage.Builder builder) throws IOException, EncodeException {
        ServerGameMessage message = builder.build();
        writer.append("[TO: " + username + "] "  + message);
        writer.flush();
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
                    OrderCardsResponse ocr = message.getOrderCardsResponse();
                    waitingResponse = false;
                    response.add(ocr.getOrderedCardsList().toArray(new String[ocr.getOrderedCardsCount()]));
                    break;
                case SELECTFROMRESPONSE:
                    SelectFromResponse sfr = message.getSelectFromResponse();
                    if(sfr.getMessageType() != selectFromType){
                        System.out.println("Wrong SelectFrom response received from " + username + 
                                "\nExpected " + selectFromType + " Received: " + message);
                        break;
                    }
                    waitingResponse = false;
                    response.add(sfr.getSelectedCardsList().toArray(new String[sfr.getSelectedCardsCount()]));
                    break;
                case SELECTPLAYERRESPONSE:
                    waitingResponse = false;
                    response.add(message.getSelectPlayerResponse().getSelectedPlayerName());
                    break;
                case SELECTXVALUERESPONSE:
                    waitingResponse = false;
                    response.add(message.getSelectXValueResponse().getSelectedXValue());
                    break;
                default:
                    System.out.println("Wrong Message received from " + username  
                            + "\nExpected " + responseType + " Received: " + message);
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
                gameServer.playCard(gameID, username, UUID.fromString(message.getPlayCard().getCardID()));
                break;
            case ACTIVATECARD:
                gameServer.activateCard(gameID, username, UUID.fromString(message.getActivateCard().getCardID()));
                break;
            case STUDYCARD:
                gameServer.studyCard(gameID, username, UUID.fromString(message.getStudyCard().getCardID()));
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
                System.out.println("Unexpected message from " + username + ": " + message);
                break;
        }
    }

    private void checkResponseType(ServerGameMessage message) {
        switch(message.getPayloadCase()){
            case ORDERCARDS:
                waitingResponse = true;
                responseType = ORDERCARDSRESPONSE;
                break;
            case SELECTFROM:
                waitingResponse = true;
                responseType = SELECTFROMRESPONSE;
                selectFromType = message.getSelectFrom().getMessageType();
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