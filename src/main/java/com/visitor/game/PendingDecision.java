package com.visitor.game;

import com.visitor.protocol.ClientGameMessages.ClientGameMessage;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.Types;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/** Match-worker-owned decision; no response can be buffered for a future prompt. */
public final class PendingDecision {
    private final UUID playerId;
    private final ServerGameMessage request;
    public final CompletableFuture<Object> answer = new CompletableFuture<>();

    public PendingDecision(UUID playerId, ServerGameMessage request) {
        this.playerId = playerId;
        this.request = request;
    }

    public boolean accept(UUID sender, ClientGameMessage message) {
        if (!playerId.equals(sender) || answer.isDone()) {
            return false;
        }
        switch (request.getPayloadCase()) {
            case SELECTFROM:
                if (message.hasSelectFromResponse() && message.getSelectFromResponse().getMessageType() == request.getSelectFrom().getMessageType()) {
                    return answer.complete(message.getSelectFromResponse().getSelectedList().toArray(new String[0]));
                }
                break;
            case SELECTXVALUE:
                if (message.hasSelectXValueResponse()) {
                    int value = message.getSelectXValueResponse().getSelectedXValue();
                    if (value >= 0 && value <= request.getSelectXValue().getMaxXValue()) {
                        return answer.complete(value);
                    }
                }
                break;
            case SELECTKNOWLEDGE:
                if (message.hasSelectKnowledgeResponse() && request.getSelectKnowledge().getKnowledgeListList().contains(message.getSelectKnowledgeResponse().getSelectedKnowledge())) {
                    return answer.complete(message.getSelectKnowledgeResponse().getSelectedKnowledge());
                }
                break;
            case ASSIGNDAMAGE:
                if (message.hasAssignDamageResponse()) {
                    return answer.complete(message.getAssignDamageResponse().getDamageAssignmentsList().toArray(new Types.DamageAssignment[0]));
                }
                break;
            case ORDERCARDS:
                if (message.hasOrderCardsResponse()) {
                    return answer.complete(message.getOrderCardsResponse().getOrderedCardsList().toArray(new String[0]));
                }
                break;
            default:
                break;
        }
        return false;
    }
}
