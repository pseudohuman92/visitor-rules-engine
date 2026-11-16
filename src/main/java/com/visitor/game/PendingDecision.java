package com.visitor.game;

import com.visitor.protocol.ClientGameMessages.ClientGameMessage;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.Types;

import java.util.HashSet;
import java.util.Set;
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
                if (message.hasSelectFromResponse()
                        && message.getSelectFromResponse().getMessageType() == request.getSelectFrom().getMessageType()) {
                    Set<String> possible = new HashSet<>(request.getSelectFrom().getTargets().getPossibleTargetsList());
                    Set<String> selected = new HashSet<>(message.getSelectFromResponse().getSelectedList());
                    int count = message.getSelectFromResponse().getSelectedCount();
                    if (count >= request.getSelectFrom().getTargets().getMinTargets()
                            && count <= request.getSelectFrom().getTargets().getMaxTargets()
                            && selected.size() == count && possible.containsAll(selected)) {
                        return answer.complete(message.getSelectFromResponse().getSelectedList().toArray(new String[0]));
                    }
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
                    Set<String> possible = new HashSet<>(request.getAssignDamage().getPossibleTargetsList());
                    Set<String> assigned = new HashSet<>();
                    long total = 0;
                    for (Types.DamageAssignment assignment : message.getAssignDamageResponse().getDamageAssignmentsList()) {
                        if (assignment.getDamage() < 0 || !possible.contains(assignment.getTargetId())
                                || !assigned.add(assignment.getTargetId())) {
                            return false;
                        }
                        total += assignment.getDamage();
                    }
                    if (total == request.getAssignDamage().getTotalDamage()) {
                        return answer.complete(message.getAssignDamageResponse().getDamageAssignmentsList().toArray(new Types.DamageAssignment[0]));
                    }
                }
                break;
            case ORDERCARDS:
                if (message.hasOrderCardsResponse()) {
                    Set<String> requested = new HashSet<>();
                    request.getOrderCards().getCardsToOrderList().forEach(card -> requested.add(card.getId()));
                    Set<String> ordered = new HashSet<>(message.getOrderCardsResponse().getOrderedCardsList());
                    if (ordered.size() == message.getOrderCardsResponse().getOrderedCardsCount()
                            && ordered.equals(requested)) {
                        return answer.complete(message.getOrderCardsResponse().getOrderedCardsList().toArray(new String[0]));
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }
}
