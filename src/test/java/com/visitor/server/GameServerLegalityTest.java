package com.visitor.server;

import com.visitor.card.Card;
import com.visitor.card.types.Cantrip;
import com.visitor.game.parts.Game;
import com.visitor.game.parts.GameTestFixture;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.ClientGameMessages;
import com.visitor.protocol.ServerGameMessages.ServerGameMessage;
import com.visitor.protocol.Types;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.visitor.protocol.Types.Knowledge.RED;
import static org.junit.Assert.*;

public class GameServerLegalityTest {
    private GameTestFixture f;
    private GameServer server,previous;
    private Capture a,b;
    private ExecutorService workers;
    private static class Capture implements GameEndpointInterface {
        final List<ServerGameMessage> messages=Collections.synchronizedList(new ArrayList<>());
        final CountDownLatch update=new CountDownLatch(1),prompt=new CountDownLatch(1);
        @Override public void send(ServerGameMessage.Builder builder,UUID target) {
            ServerGameMessage message=builder.build(); messages.add(message);
            if (message.hasUpdateGameStateP()) update.countDown();
            if (message.hasSelectXValue()) prompt.countDown();
        }
        @Override public void close() {}
        @Override public void resendLastMessage(UUID target) {}
    }
    @Before public void setUp() {
        f=new GameTestFixture(); server=new GameServer();
        previous=GeneralEndpoint.gameServer; GeneralEndpoint.gameServer=server;
        server.games.put(f.game.getId(),f.game);
        a=new Capture(); b=new Capture();
        f.game.addConnection(f.player.getId(),a); f.game.addConnection(f.opponent.getId(),b);
        workers=Executors.newFixedThreadPool(2);
    }
    @After public void tearDown() throws Exception {
        workers.shutdownNow(); workers.awaitTermination(2,TimeUnit.SECONDS);
        f.close(); GeneralEndpoint.gameServer=previous;
    }
    @Test public void rejectedPlayRefreshesOnlySenderAndRestartsClock() {
        Card card=f.player.hand.get(0); String before=f.snapshot();
        server.playCard(f.game.getId(),f.player.getId(),card.getId(),new Arraylist<>()).join();
        assertEquals(before,f.snapshot()); assertTrue(f.player.clock.isActive());
        assertEquals(1,a.messages.size()); assertTrue(b.messages.isEmpty());
        assertFalse(a.messages.get(0).getUpdateGameStateP().getGame().getPlayer().getHand(0).getCanPlay());
    }
    @Test public void legalActionsContinueAfterARejectedCommand() {
        server.playCard(f.game.getId(),f.player.getId(),f.player.hand.get(0).getId(),new Arraylist<>()).join();
        Card studied=f.player.hand.get(0);
        server.studyCard(f.game.getId(),f.player.getId(),studied.getId(),new CounterMap<>(RED,1)).join();
        assertTrue(f.player.hand.contains(studied));
        assertFalse(studied.canPlay(true));
        Card played=f.player.hand.get(1);
        server.playCard(f.game.getId(),f.player.getId(),played.getId(),new Arraylist<>()).join();
        assertEquals(0,f.player.getEnergy());
        assertTrue(f.game.toGameState(f.player.getId(),false).getStackList().stream().anyMatch(c -> c.getId().equals(played.getId().toString())));
        assertEquals(2,b.messages.size());
    }
    @Test public void concurrentCopiesOfACommandCannotSpendOrPlayTwice() throws Exception {
        f.player.addEnergy(1,false); f.player.addKnowledge(new CounterMap<>(RED,1));
        Card card=f.player.hand.get(0); CountDownLatch start=new CountDownLatch(1);
        Runnable action=() -> {
            try { start.await(); } catch(InterruptedException e) { throw new RuntimeException(e); }
            server.playCard(f.game.getId(),f.player.getId(),card.getId(),new Arraylist<>()).join();
        };
        Future<?> first=workers.submit(action),second=workers.submit(action); start.countDown();
        first.get(3,TimeUnit.SECONDS); second.get(3,TimeUnit.SECONDS);
        assertEquals(0,f.player.getEnergy());
        assertEquals(1,f.game.toGameState(f.player.getId(),false).getStackCount());
        assertFalse(f.player.hand.contains(card));
    }
    @Test public void websocketCombatCommandRetainsItsActualSender() throws Exception {
        Card unit=f.play(new com.visitor.sets.base2.Stormtrooper(f.game,f.player.getId()),f.player);
        f.setPhase(Types.Phase.ATTACK,f.player.getId(),f.player.getId());
        String before=f.snapshot();
        GameEndpoint endpoint=new GameEndpoint();
        endpoint.gameID=f.game.getId(); endpoint.playerId=f.opponent.getId();
        ClientGameMessages.ClientGameMessage message=ClientGameMessages.ClientGameMessage.newBuilder()
                .setSelectAttackers(ClientGameMessages.SelectAttackers.newBuilder().addAttackers(
                        Types.AttackerAssignment.newBuilder().setAttackerId(unit.getId().toString()).setAttacksTo(f.opponent.getId().toString())))
                .build();
        endpoint.onMessage(null,message.toByteArray());
        assertTrue(b.update.await(3,TimeUnit.SECONDS));
        f.game.executeAction(() -> {}).get(3,TimeUnit.SECONDS);
        assertEquals(before,f.snapshot());
        assertFalse(unit.isDepleted()); assertTrue(a.messages.isEmpty());
    }
    @Test public void promptResponsesCanUnblockAnActionOnTheMatchWorker() throws Exception {
        AtomicInteger selected=new AtomicInteger(-1);
        Card spell=f.hand(new PromptSpell(f.game,f.player.getId(),selected),f.player);
        server.playCard(f.game.getId(),f.player.getId(),spell.getId(),new Arraylist<>()).join();
        server.pass(f.game.getId(),f.opponent.getId()).join();
        java.util.concurrent.CompletableFuture<Void> resolve=server.pass(f.game.getId(),f.player.getId());
        assertTrue(a.prompt.await(3,TimeUnit.SECONDS));
        server.handleGameMessage(f.game.getId(),f.player.getId(),ClientGameMessages.ClientGameMessage.newBuilder()
                .setSelectXValueResponse(ClientGameMessages.SelectXValueResponse.newBuilder().setSelectedXValue(2)).build()).get(3,TimeUnit.SECONDS);
        resolve.get(3,TimeUnit.SECONDS);
        assertEquals(2,selected.get());
        assertEquals(0,f.game.toGameState(f.player.getId(),false).getStackCount());
        assertTrue(f.player.clock.isActive());
    }
    private static class PromptSpell extends Cantrip {
        PromptSpell(Game game,UUID owner,AtomicInteger selected) {
            super(game,"Choose X",0,null,"",owner);
            playable.addResolveEffect(() -> selected.set(game.selectX(owner,3)));
        }
    }
}
