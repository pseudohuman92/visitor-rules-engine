package com.visitor.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.visitor.game.AiPlayer;
import com.visitor.protocol.ClientGameMessages.*;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.AttackerAssignment;
import com.visitor.protocol.Types.BlockerAssignment;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
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
@ServerEndpoint(value = "/ai/{playerId}/{gameID}/{aiId}")
public class AiEndpoint implements GameEndpointInterface {

    Session session;
    UUID playerId;
    UUID gameID;
    boolean waitingResponse;
    ClientGameMessage.PayloadCase responseType;
    Types.SelectFromType selectFromType;
    UUID aiId;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameID") String gameID, @PathParam("playerId") String playerId, @PathParam("aiId") String aiId) throws IOException, EncodeException {
        this.session = session;
        this.playerId = UUID.fromString(playerId);
        this.gameID = UUID.fromString(gameID);
        this.aiId = UUID.fromString(aiId);
        session.getBasicRemote().setBatchingAllowed(false);
        session.getAsyncRemote().setBatchingAllowed(false);
        session.setMaxIdleTimeout(0);
        gameServer.addGameConnection(this.gameID, this.playerId, this);
        gameServer.addGameConnection(this.gameID, this.aiId, this);
        resendLastMessage(this.playerId);
        resendLastMessage(this.aiId);
    }


    @OnMessage
    public void onMessage(Session session, byte[] message) {
        new Thread(() -> {
            try {
                ClientGameMessage cgm = ClientGameMessage.parseFrom(message);
                processClientGameMessage(cgm, playerId);
            } catch (InvalidProtocolBufferException ex) {
                getLogger(AiEndpoint.class.getName()).log(SEVERE, null, ex);
            }
        }).start();
    }


    @OnClose
    public void onClose(Session session) {
        gameServer.removeGameConnection(gameID, playerId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        out.println("[ERROR: " + playerId + "] " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void send(ServerGameMessage.Builder builder, UUID targetId) throws IOException, EncodeException {
        ServerGameMessage message = builder.build();
        out.println("[Message to " + (playerId.equals(targetId) ? "player" : "AI") + "]\n" + message);
        gameServer.appendToHistory(gameID, message);
        checkResponseType(message);

        if (playerId.equals(targetId)) {
            session.getBasicRemote().sendObject(message.toByteArray());
        } else {
            ClientGameMessage aiResponse = AiPlayer.getResponse(message, aiId);
            out.println("[AI Response] " + aiResponse);
            if (aiResponse != null) {
                processClientGameMessage(aiResponse, aiId);
            }
        }
    }


    public void close() {
        session = null;
    }

    public void resendLastMessage(UUID targetId) throws IOException, EncodeException {
        ServerGameMessage lastMessage = gameServer.getLastMessage(gameID, targetId);
        if (playerId.equals(targetId)) {
            session.getBasicRemote().sendObject(lastMessage.toByteArray());
        } else {
            ClientGameMessage aiResponse = AiPlayer.getResponse(lastMessage, aiId);
            out.println("[AI Response] " + aiResponse);
            if (aiResponse != null) {
                processClientGameMessage(aiResponse, aiId);
            }
        }
    }

    private void processClientGameMessage(ClientGameMessage message, UUID senderId) {
        gameServer.appendToHistory(gameID, message);
        if (waitingResponse) {
            processResponse(message, senderId);
        } else {
            processMessage(message, senderId);
        }
    }

    private void processMessage(ClientGameMessage message, UUID senderId) {
        switch (message.getPayloadCase()) {
            case PLAYCARD:
                gameServer.playCard(gameID, senderId, fromString(message.getPlayCard().getCardID()));
                break;
            case ACTIVATECARD:
                gameServer.activateCard(gameID, senderId, fromString(message.getActivateCard().getCardID()));
                break;
            case STUDYCARD:
                gameServer.studyCard(gameID, senderId, fromString(message.getStudyCard().getCardID()));
                break;
            case PASS:
                gameServer.pass(gameID, senderId);
                break;
            case REDRAW:
                gameServer.redraw(gameID, senderId);
                break;
            case KEEP:
                gameServer.keep(gameID, senderId);
                break;
            case CONCEDE:
                gameServer.concede(gameID, senderId);
                break;
            default:
                out.println("Unexpected message from " + senderId + ": " + message);
                break;
        }
    }

    private void processResponse(ClientGameMessage message, UUID senderId) {
        if (message.getPayloadCase() == CONCEDE) {
            gameServer.concede(gameID, senderId);
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
                        out.println("Wrong SelectFrom response received from " + senderId +
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
                    AssignDamageResponse ddr = message.getAssignDamageResponse();
                    waitingResponse = false;
                    gameServer.addToResponseQueue(gameID, ddr.getDamageAssignmentsList().toArray(new Types.DamageAssignment[ddr.getDamageAssignmentsCount()]));
                    break;
                case SELECTKNOWLEDGERESPONSE:
                    SelectKnowledgeResponse skr = message.getSelectKnowledgeResponse();
                    waitingResponse = false;
                    gameServer.addToResponseQueue(gameID, skr.getSelectedKnowledge());
                    break;
                default:
                    out.println("Wrong Message received from " + senderId
                            + "\nExpected " + responseType + " Received: " + message);
                    break;
            }
            waitingResponse = false;
        } else {
            out.println("Unexpected response from " + senderId + ": " + message);
        }
    }


    private void checkResponseType(ServerGameMessage message) {
        waitingResponse = true;
        switch (message.getPayloadCase()) {
            case ORDERCARDS:
                responseType = ORDERCARDSRESPONSE;
                break;
            case SELECTFROM:
                responseType = SELECTFROMRESPONSE;
                selectFromType = message.getSelectFrom().getMessageType();
                break;
            case SELECTXVALUE:
                responseType = SELECTXVALUERESPONSE;
                break;
            case SELECTATTACKERS:
                responseType = SELECTATTACKERSRESPONSE;
                break;
            case SELECTBLOCKERS:
                responseType = SELECTBLOCKERSRESPONSE;
                break;
            case ASSIGNDAMAGE:
                responseType = ASSIGNDAMAGERESPONSE;
                break;
            case SELECTKNOWLEDGE:
                responseType = SELECTKNOWLEDGERESPONSE;
                break;
            default:
                waitingResponse = false;
                break;
        }
    }
}
