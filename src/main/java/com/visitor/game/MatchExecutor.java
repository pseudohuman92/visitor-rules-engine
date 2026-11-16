package com.visitor.game;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/** One owner thread per active match. Ordinary actions never run inside another action. */
public final class MatchExecutor implements AutoCloseable {
    private final ExecutorService worker;
    private final BlockingQueue<Runnable> controls = new LinkedBlockingQueue<>();
    private volatile Thread owner;
    private volatile CompletableFuture<?> waiting;

    public MatchExecutor(String name) {
        worker = Executors.newSingleThreadExecutor(task -> {
            Thread thread = new Thread(task, "visitor-match-" + name);
            thread.setDaemon(true);
            owner = thread;
            return thread;
        });
    }

    public boolean isOwnerThread() {
        return Thread.currentThread() == owner;
    }

    public CompletableFuture<Void> action(Runnable action) {
        CompletableFuture<Void> result = new CompletableFuture<>();
        try {
            worker.execute(wrap(action, result));
        } catch (RejectedExecutionException e) {
            result.completeExceptionally(e);
        }
        return result;
    }

    /** Responses, connections and termination may run while an action awaits a decision. */
    public CompletableFuture<Void> control(Runnable action) {
        CompletableFuture<Void> result = new CompletableFuture<>();
        Runnable task = wrap(action, result);
        if (isOwnerThread()) {
            task.run();
        } else {
            controls.add(task);
            try {
                worker.execute(() -> {
                    if (controls.remove(task)) task.run();
                });
            } catch (RejectedExecutionException e) {
                if (controls.remove(task)) {
                    result.completeExceptionally(e);
                }
            }
        }
        return result;
    }

    private Runnable wrap(Runnable action, CompletableFuture<Void> result) {
        return () -> {
            try {
                action.run();
                result.complete(null);
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        };
    }

    public <T> T await(CompletableFuture<T> decision) {
        if (!isOwnerThread()) {
            throw new IllegalStateException("Decisions must be awaited on the match worker");
        }
        waiting = decision;
        try {
            while (!decision.isDone()) {
                Runnable task = controls.poll(100, TimeUnit.MILLISECONDS);
                if (task != null) {
                    task.run();
                }
            }
            return decision.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CancellationException("Match worker interrupted");
        } finally {
            waiting = null;
        }
    }

    @Override
    public void close() {
        CompletableFuture<?> decision = waiting;
        if (decision != null) {
            decision.cancel(false);
        }
        worker.shutdown();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return worker.awaitTermination(timeout, unit);
    }
}
