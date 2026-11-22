package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.game.Event;
import com.visitor.game.Player;
import com.visitor.game.MatchExecutor;
import com.visitor.game.PendingDecision;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.Types.Phase;
import com.visitor.server.GameEndpointInterface;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CompletionException;
import java.util.concurrent.RejectedExecutionException;

import static java.util.UUID.randomUUID;

public class Base implements AutoCloseable {
    Arraylist<String> history;

    public transient Hashmap<UUID, GameEndpointInterface> connections;
    public UUID activePlayer;
    protected PendingDecision pendingDecision;
    private final MatchExecutor executor;
    protected volatile boolean ended;
    private volatile boolean closed;
    Hashmap<UUID, Player> players;
    ConcurrentMap<UUID, ServerGameMessage> lastMessages;
    UUID turnPlayer;
    Arraylist<Card> stack;
    Card currentlyResolving;
    Phase phase;
    int turnCount;
    int passCount;
    UUID id;
    Hashmap<UUID, Arraylist<Card>> triggeringCards;
    Arraylist<Event> eventQueue;

    Arraylist<Card> toDeregister;
    private int costPaymentDepth;

    Arraylist<UUID> attackers;
    Arraylist<UUID> blockers;

    public Base() {
        history = new Arraylist<>();
        id = randomUUID();
        executor = new MatchExecutor(id.toString());
        players = new Hashmap<>();
        connections = new Hashmap<UUID, GameEndpointInterface>();
        stack = new Arraylist<>();
        lastMessages = new ConcurrentHashMap<>();
        triggeringCards = new Hashmap<>();
        toDeregister = new Arraylist<>();

        eventQueue = new Arraylist<>();
        attackers = new Arraylist<>();
        blockers = new Arraylist<>();
    }

    public void addToHistory(Object message) {
        history.add(message.toString());
    }

    public void payAdditionalCosts(Runnable action) {
        costPaymentDepth++;
        try {
            action.run();
        } finally {
            costPaymentDepth--;
        }
    }

    public boolean isPayingAdditionalCost() {
        return costPaymentDepth > 0;
    }

    public CompletableFuture<Void> executeAction(Runnable action) {
        if (ended) return CompletableFuture.completedFuture(null);
        return executor.action(() -> {
            if (!ended) action.run();
        });
    }

    public CompletableFuture<Void> executeControl(Runnable control) {
        return executor.control(control);
    }

    public boolean isMatchThread() {
        return executor.isOwnerThread();
    }

    public boolean isEnded() {
        return ended;
    }

    protected Object awaitDecision(PendingDecision decision) {
        return executor.await(decision.answer);
    }

    @Override
    public void close() {
        if (closed) return;
        CompletableFuture<Void> completion = executeControl(() -> {
            if (closed) return;
            closed = true;
            ended = true;
            if (pendingDecision != null) pendingDecision.answer.cancel(false);
            players.values().forEach(player -> player.clock.interrupt());
            executor.close();
        });
        if (!isMatchThread()) {
            try {
                completion.join();
            } catch (CompletionException e) {
                if (!closed || !(e.getCause() instanceof RejectedExecutionException)) throw e;
            }
        }
    }

    public enum Zone {
        Deck, Hand, Play, Both_Play, Discard_Pile, None, Players, Play_With_Player, Both_Play_With_Players, Stack
    }
}
