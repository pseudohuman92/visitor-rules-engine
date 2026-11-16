package com.visitor.server;

import com.visitor.card.Card;
import com.visitor.card.types.Cantrip;
import com.visitor.game.ClockTestSupport;
import com.visitor.game.parts.Game;
import com.visitor.game.parts.GameTestFixture;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.ClientGameMessages;
import com.visitor.protocol.ClientGameMessages.ClientGameMessage;
import com.visitor.protocol.ServerGameMessages;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.visitor.protocol.Types.Knowledge.RED;
import static org.junit.Assert.*;

public class MatchSerializationTest {
    private GameServer server, previous;
    private RecordingGame game;
    private GameTestFixture f;
    private Capture a,b;
    private final AtomicInteger selected = new AtomicInteger(-1);

    private static class RecordingGame extends Game {
        volatile boolean recording;
        final Set<Thread> writers = ConcurrentHashMap.newKeySet();
        private void mark() {
            if (recording) {
                assertTrue("Mutation must be on match worker",isMatchThread());
                writers.add(Thread.currentThread());
            }
        }
        @Override public void addToHistory(Object message) { mark(); super.addToHistory(message); }
        @Override public void addConnection(UUID player,GameEndpointInterface endpoint) { mark(); super.addConnection(player,endpoint); }
        @Override public void removeConnection(UUID player,GameEndpointInterface endpoint) { mark(); super.removeConnection(player,endpoint); }
        @Override public void setLastMessage(UUID player,ServerGameMessage message) { mark(); super.setLastMessage(player,message); }
        @Override public void playCard(UUID player,UUID card,Arraylist<com.visitor.protocol.Types.TargetSelection> targets) { mark(); super.playCard(player,card,targets); }
        @Override public void studyCard(UUID player,UUID card,boolean regular,CounterMap<com.visitor.protocol.Types.Knowledge> gain) { mark(); super.studyCard(player,card,regular,gain); }
        @Override public void gameEnd(UUID player,boolean win) { mark(); super.gameEnd(player,win); }
    }

    private class Capture implements GameEndpointInterface {
        final List<ServerGameMessage> messages = new CopyOnWriteArrayList<>();
        final BlockingQueue<ServerGameMessage> prompts = new LinkedBlockingQueue<>();
        @Override public void send(ServerGameMessage.Builder builder,UUID target) {
            assertTrue(game.isMatchThread());
            ServerGameMessage message = builder.build();
            messages.add(message);
            if (message.hasSelectXValue()) prompts.add(message);
        }
        @Override public void close() { assertTrue(game.isMatchThread()); }
        @Override public void resendLastMessage(UUID target) {
            ServerGameMessage last = server.getLastMessage(game.getId(),target);
            if (last != null) send(last.toBuilder(),target);
        }
    }

    @Before public void setUp() throws Exception {
        game = new RecordingGame(); f = new GameTestFixture(game);
        server = new GameServer(); previous = GeneralEndpoint.gameServer; GeneralEndpoint.gameServer = server;
        server.games.put(game.getId(),game);
        game.recording = true;
        a = new Capture(); b = new Capture();
        done(server.addGameConnection(game.getId(),f.player.getId(),a));
        done(server.addGameConnection(game.getId(),f.opponent.getId(),b));
        done(game.executeControl(game::updatePlayers));
        a.messages.clear(); b.messages.clear();
    }
    @After public void tearDown() throws Exception { f.close(); GeneralEndpoint.gameServer = previous; }
    private void done(CompletableFuture<?> future) throws Exception { future.get(3,TimeUnit.SECONDS); }
    private void await(CountDownLatch latch) {
        try { assertTrue(latch.await(3,TimeUnit.SECONDS)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new AssertionError(e); }
    }
    private ClientGameMessage answer(int value) {
        return ClientGameMessage.newBuilder().setSelectXValueResponse(
                ClientGameMessages.SelectXValueResponse.newBuilder().setSelectedXValue(value)).build();
    }
    private CompletableFuture<Void> prompt() throws Exception {
        AtomicReference<Card> spell = new AtomicReference<>();
        done(game.executeAction(() -> spell.set(f.hand(new PromptSpell(game,f.player.getId(),selected),f.player))));
        done(server.playCard(game.getId(),f.player.getId(),spell.get().getId(),new Arraylist<>()));
        done(server.pass(game.getId(),f.opponent.getId()));
        CompletableFuture<Void> resolve = server.pass(game.getId(),f.player.getId());
        assertNotNull(a.prompts.poll(3,TimeUnit.SECONDS));
        return resolve;
    }
    private static class PromptSpell extends Cantrip {
        PromptSpell(Game game,UUID owner,AtomicInteger selected) {
            super(game,"Decision",0,null,"",owner);
            playable.addResolveEffect(() -> selected.set(game.selectX(owner,3)));
        }
    }

    @Test public void actionsConnectionsAndHistoryWaitForTheSameOwner() throws Exception {
        CountDownLatch started=new CountDownLatch(1), release=new CountDownLatch(1);
        CompletableFuture<Void> blocker=game.executeAction(() -> { started.countDown(); await(release); });
        await(started);
        try {
            CompletableFuture<Void> history=server.appendToHistory(game.getId(),"queued history");
            Capture fresh=new Capture();
            CompletableFuture<Void> connection=server.addGameConnection(game.getId(),f.player.getId(),fresh);
            CompletableFuture<Void> study=server.studyCard(game.getId(),f.player.getId(),f.player.hand.get(0).getId(),new CounterMap<>(RED,1));
            assertFalse(history.isDone()); assertFalse(connection.isDone()); assertFalse(study.isDone());
            release.countDown(); done(blocker); done(history); done(connection); done(study);
            assertEquals(1,f.player.getMaxEnergy());
            assertSame(fresh,game.connections.get(f.player.getId()));
            assertEquals(1,game.writers.size());
        } finally { release.countDown(); }
    }

    @Test public void normalActionsAndControlsKeepTheirQueueOrderOutsideADecision() throws Exception {
        CountDownLatch started=new CountDownLatch(1), release=new CountDownLatch(1);
        CompletableFuture<Void> blocker=game.executeAction(() -> { started.countDown(); await(release); });
        await(started);
        List<String> order=new CopyOnWriteArrayList<>();
        try {
            CompletableFuture<Void> first=game.executeControl(() -> order.add("control-1"));
            CompletableFuture<Void> middle=game.executeAction(() -> order.add("action"));
            CompletableFuture<Void> last=game.executeControl(() -> order.add("control-2"));
            release.countDown(); done(blocker); done(first); done(middle); done(last);
            assertEquals(java.util.Arrays.asList("control-1","action","control-2"),order);
        } finally { release.countDown(); }
    }

    @Test public void differentMatchesProgressIndependently() throws Exception {
        CountDownLatch started=new CountDownLatch(1), release=new CountDownLatch(1);
        CompletableFuture<Void> first=game.executeAction(() -> { started.countDown(); await(release); });
        await(started);
        try (GameTestFixture other=new GameTestFixture()) {
            server.games.put(other.game.getId(),other.game);
            AtomicReference<Thread> secondWorker=new AtomicReference<>();
            done(server.studyCard(other.game.getId(),other.player.getId(),other.player.hand.get(0).getId(),new CounterMap<>(RED,1)));
            done(other.game.executeAction(() -> secondWorker.set(Thread.currentThread())));
            assertEquals(1,other.player.getMaxEnergy());
            assertFalse(first.isDone());
            assertFalse(game.writers.contains(secondWorker.get()));
        } finally { release.countDown(); done(first); }
    }

    @Test public void ordinaryActionsStayQueuedWhileResponsesResumeTheCurrentAction() throws Exception {
        CompletableFuture<Void> resolving=prompt();
        CompletableFuture<Void> study=server.studyCard(game.getId(),f.player.getId(),f.player.hand.get(0).getId(),new CounterMap<>(RED,1));
        assertFalse(study.isDone());
        done(server.handleGameMessage(game.getId(),f.player.getId(),answer(2)));
        done(resolving); done(study);
        assertEquals(2,selected.get()); assertEquals(1,f.player.getMaxEnergy());
        assertEquals(1,game.writers.size());
    }

    @Test public void reconnectReplaysPendingPromptAndOldDisconnectCannotRemoveIt() throws Exception {
        CompletableFuture<Void> resolving=prompt();
        Capture fresh=new Capture();
        done(server.addGameConnection(game.getId(),f.player.getId(),fresh));
        assertNotNull(fresh.prompts.poll(3,TimeUnit.SECONDS));
        done(server.removeGameConnection(game.getId(),f.player.getId(),a));
        assertSame(fresh,game.connections.get(f.player.getId()));
        done(server.handleGameMessage(game.getId(),f.player.getId(),answer(1)));
        done(resolving);
        assertEquals(1,selected.get());
        assertTrue(fresh.messages.get(fresh.messages.size()-1).hasUpdateGameStateP());
        assertEquals(1,game.writers.size());
    }

    @Test public void wrongDuplicateAndUnsolicitedResponsesDoNotLeakIntoTheNextPrompt() throws Exception {
        done(server.handleGameMessage(game.getId(),f.player.getId(),answer(3)));
        CompletableFuture<Void> resolving=prompt();
        done(server.handleGameMessage(game.getId(),f.opponent.getId(),answer(1)));
        done(server.handleGameMessage(game.getId(),f.player.getId(),answer(100)));
        assertFalse(resolving.isDone());
        done(server.handleGameMessage(game.getId(),f.player.getId(),answer(2)));
        done(server.handleGameMessage(game.getId(),f.player.getId(),answer(3)));
        done(resolving); assertEquals(2,selected.get());
        CompletableFuture<Void> second=prompt();
        assertFalse(second.isDone());
        done(server.handleGameMessage(game.getId(),f.player.getId(),answer(1)));
        done(second); assertEquals(1,selected.get());
    }

    @Test public void concessionCancelsPendingResolutionAndDropsQueuedActions() throws Exception {
        CompletableFuture<Void> resolving=prompt();
        CompletableFuture<Void> queued=server.studyCard(game.getId(),f.player.getId(),f.player.hand.get(0).getId(),new CounterMap<>(RED,1));
        done(server.concede(game.getId(),f.player.getId())); done(resolving); done(queued);
        assertTrue(game.isEnded()); assertFalse(server.games.containsKey(game.getId()));
        assertEquals(-1,selected.get()); assertEquals(0,f.player.getMaxEnergy());
        assertEquals(1,a.messages.stream().filter(ServerGameMessage::hasGameEnd).count());
        assertEquals(1,b.messages.stream().filter(ServerGameMessage::hasGameEnd).count());
        done(server.concede(game.getId(),f.player.getId()));
        assertEquals(1,game.writers.size());
    }

    @Test public void timeoutEndsAPromptOnTheMatchWorkerAndStopsItsClocks() throws Exception {
        CompletableFuture<Void> resolving=prompt();
        done(game.executeControl(() -> ClockTestSupport.expireOnNextTick(f.player.clock)));
        done(resolving);
        assertTrue(game.isEnded()); assertFalse(server.games.containsKey(game.getId()));
        assertEquals(-1,selected.get());
        f.player.clock.join(1000); f.opponent.clock.join(1000);
        assertFalse(f.player.clock.isAlive()); assertFalse(f.opponent.clock.isAlive());
        assertEquals(1,game.writers.size());
    }

    @Test public void cachedSnapshotsRemainReadableWhileAnActionIsIncomplete() throws Exception {
        CountDownLatch started=new CountDownLatch(1), release=new CountDownLatch(1);
        CompletableFuture<Void> action=game.executeAction(() -> {
            f.player.damagable.setHealth(5); started.countDown(); await(release); game.updatePlayers();
        });
        await(started);
        try {
            assertEquals(30,server.getLastMessage(game.getId(),f.player.getId()).getUpdateGameStateP().getGame().getPlayer().getHealth());
        } finally { release.countDown(); }
        done(action);
        assertEquals(5,server.getLastMessage(game.getId(),f.player.getId()).getUpdateGameStateP().getGame().getPlayer().getHealth());
    }

    @Test public void aiCommandsAreQueuedInsteadOfMutatingDuringASendCallback() throws Exception {
        AiEndpoint endpoint=new AiEndpoint(); endpoint.gameID=game.getId(); endpoint.playerId=f.player.getId(); endpoint.aiId=f.opponent.getId();
        done(server.addGameConnections(game.getId(),endpoint,f.player.getId(),f.opponent.getId()));
        done(game.executeAction(() -> {
            game.activePlayer=f.opponent.getId();
            try {
                endpoint.send(ServerGameMessage.newBuilder().setUpdateGameStateP(
                        ServerGameMessages.UpdateGameStateP.newBuilder().setGame(game.toGameState(f.opponent.getId(),false))),f.opponent.getId());
            } catch (Exception e) { throw new AssertionError(e); }
            assertEquals("AI must not re-enter an in-progress action",f.opponent.getId(),game.activePlayer);
        }));
        done(game.executeAction(() -> {}));
        assertEquals(f.player.getId(),game.activePlayer);
        AtomicInteger choice=new AtomicInteger(-1);
        done(game.executeAction(() -> choice.set(game.selectX(f.opponent.getId(),3))));
        assertTrue(choice.get()>=0 && choice.get()<=3);
        assertEquals(1,game.writers.size());
    }

    @Test public void workerTerminatesAfterClosingTheMatch() throws Exception {
        AtomicReference<Thread> owner=new AtomicReference<>();
        done(game.executeAction(() -> owner.set(Thread.currentThread())));
        game.close(); owner.get().join(1000);
        assertFalse(owner.get().isAlive());
        assertTrue(game.isEnded());
        done(game.executeAction(() -> fail("Closed game must not mutate")));
    }
}
