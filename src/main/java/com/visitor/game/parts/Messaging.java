package com.visitor.game.parts;

import com.visitor.card.properties.Targetable;
import com.visitor.card.Card;
import com.visitor.game.Player;
import com.visitor.game.PendingDecision;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.protocol.ServerGameMessages;
import com.visitor.protocol.Types;
import com.visitor.server.GameEndpointInterface;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.visitor.game.parts.Base.Zone.Both_Play;
import static com.visitor.helpers.UUIDHelper.toUUIDList;
import static com.visitor.protocol.Types.SelectFromType.LIST;
import static com.visitor.server.GeneralEndpoint.gameServer;
import static java.lang.System.out;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class Messaging extends Connections {
    /**
     * Client Prompt Methods
     * When you need client to do something.
     */
    //// Helpers
    private void send(UUID playerId, ServerGameMessages.ServerGameMessage.Builder builder) {
        try {
            setLastMessage(playerId, builder.build());
            GameEndpointInterface e = connections.get(playerId);

            if (e != null) {
                e.send(builder, playerId);
            }
        } catch (IOException | EncodeException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
    }

    private Object ask(UUID playerId, ServerGameMessages.ServerGameMessage.Builder builder) {
        if (pendingDecision != null) throw new IllegalStateException("Another decision is already pending");
        PendingDecision decision = new PendingDecision(playerId, builder.build());
        pendingDecision = decision;
        Player choosingPlayer = getPlayer(playerId);
        choosingPlayer.clock.activate();
        try {
            send(playerId, builder);
            return awaitDecision(decision);
        } finally {
            choosingPlayer.clock.pause();
            pendingDecision = null;
        }
    }

    private Arraylist<UUID> selectFrom(UUID playerId, Types.SelectFromType type, Arraylist<Targetable> candidates, Arraylist<UUID> canSelect,
                                       Arraylist<UUID> canSelectPlayers, int minCount, int maxCount, String message) {
        Types.Targeting.Builder t = Types.Targeting.newBuilder()
                .addAllPossibleTargets(canSelect.transformToStringList())
                .addAllPossibleTargets(canSelectPlayers.transformToStringList())
                .setMinTargets(minCount)
                .setMaxTargets(maxCount)
                .setTargetMessage(message);

        ServerGameMessages.SelectFrom.Builder b = ServerGameMessages.SelectFrom.newBuilder()
                .setTargets(t)
                .setMessageType(type)
                .setGame(toGameState(playerId, true));
        String[] selected = (String[]) ask(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setSelectFrom(b));
        return toUUIDList(selected);
    }

    Arraylist<Types.DamageAssignment> assignDamage(UUID playerId, UUID id, Arraylist<UUID> possibleTargets, int damage, boolean trample) {
        out.println("Sending Assign Damage Message to " + playerId);
        ServerGameMessages.AssignDamage.Builder b = ServerGameMessages.AssignDamage.newBuilder()
                .setDamageSource(id.toString())
                .addAllPossibleTargets(possibleTargets.transformToStringList())
                .setTotalDamage(damage)
                .setTrample(trample)
                .setGame(toGameState(playerId, true));
        Types.DamageAssignment[] selected = (Types.DamageAssignment[]) ask(playerId,
                ServerGameMessages.ServerGameMessage.newBuilder().setAssignDamage(b));
        return new Arraylist<>(selected);
    }

    //// Prompters
    public Arraylist<UUID> selectFromZone(UUID playerId, Game.Zone zone, Predicate<Targetable> validTarget,
                                          int minCount, int maxCount, String message) {
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(playerId, zone).parallelStream()
                .filter(validTarget).map(Targetable::getId).collect(Collectors.toList()));
        return selectFrom(playerId, getZoneLabel(zone), ((Arraylist<Targetable>)getZone(playerId, zone)), canSelect, new Arraylist<>(), minCount, maxCount, message);
    }

    public Arraylist<UUID> selectFromZoneWithPlayers(UUID playerId, Game.Zone zone, Predicate<Targetable> validTarget,
                 Predicate<com.visitor.game.Player> validPlayer, int minCount, int maxCount, String message) {
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(playerId, zone).parallelStream()
                .filter(validTarget).map(Targetable::getId).collect(Collectors.toList()));
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(Player::getId).collect(Collectors.toList()));
        return selectFrom(playerId, getZoneLabel(zone), ((Arraylist<Targetable>)getZone(playerId, zone)), canSelect, canSelectPlayers, minCount, maxCount, message);
    }

    public Arraylist<UUID> selectFromList(UUID playerId, Arraylist<Targetable> candidates, Predicate<Targetable> validTarget, int minCount, int maxCount, String message) {
        if (message == null || message.isEmpty()) {
            message = "Select between " + minCount + " and " + maxCount;
        }
        Arraylist<UUID> canSelect = new Arraylist<>(candidates.parallelStream()
                .filter(validTarget).map(Targetable::getId).collect(Collectors.toList()));
        return selectFrom(playerId, LIST, candidates, canSelect, new Arraylist<>(), minCount, maxCount, message);
    }

    public Arraylist<UUID> selectPlayers(UUID playerId, Predicate<com.visitor.game.Player> validPlayer, int minCount, int maxCount) {
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(Player::getId).collect(Collectors.toList()));
        String message = "Select between " + minCount + " and " + maxCount + " players.";
        return selectFrom(playerId, getZoneLabel(Game.Zone.Play), new Arraylist<>(), new Arraylist<>(), canSelectPlayers, minCount, maxCount, message);
    }

    public Arraylist<UUID> selectDamageTargetsConditional(UUID playerId, Predicate<Targetable> validTarget,
                              Predicate<com.visitor.game.Player> validPlayer, int minCount, int maxCount, String message) {
        return selectFromZoneWithPlayers(playerId, Both_Play, validTarget, validPlayer, minCount, maxCount, message);
    }

    /*
    public Arraylist<UUID> selectDamageTargets(UUID playerId, int minCount, int maxCount, String message) {
        return selectFromZoneWithPlayers(playerId, Both_Play, Predicates::isDamageable, Predicates::any, minCount, maxCount, message);
    }
    */

    public int selectX(UUID playerId, int maxX) {
        if (maxX == 0) {
            return maxX;
        }
        ServerGameMessages.SelectXValue.Builder b = ServerGameMessages.SelectXValue.newBuilder()
                .setMaxXValue(maxX)
                .setGame(toGameState(playerId, true));
        return (int) ask(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setSelectXValue(b));
    }

    public CounterMap<Types.Knowledge> selectKnowledge(UUID playerId, Set<Types.Knowledge> knowledgeSet) {
        ServerGameMessages.SelectKnowledge.Builder b = ServerGameMessages.SelectKnowledge.newBuilder()
                .addAllKnowledgeList(knowledgeSet)
                .setGame(toGameState(playerId, true));
        out.println(b.build());
        return new CounterMap<>((Types.Knowledge) ask(playerId,
                ServerGameMessages.ServerGameMessage.newBuilder().setSelectKnowledge(b)), 1);
    }

    public void gameEnd(UUID playerId, boolean win) {
        if (ended) return;
        if (!isMatchThread()) {
            executeControl(() -> gameEnd(playerId, win));
            return;
        }
        if (ended) return;
        ended = true;
        try {
            send(playerId, ServerGameMessages.ServerGameMessage.newBuilder()
                    .setGameEnd(ServerGameMessages.GameEnd.newBuilder()
                            .setGame(toGameState(playerId, true)).setWin(win)));
            send(getOpponentId(playerId), ServerGameMessages.ServerGameMessage.newBuilder()
                    .setGameEnd(ServerGameMessages.GameEnd.newBuilder()
                            .setGame(toGameState(getOpponentId(playerId), true)).setWin(!win)));
        } finally {
            try {
                connections.values().stream().distinct().forEach(GameEndpointInterface::close);
            } finally {
                connections.clear();
                if (gameServer != null) gameServer.removeGame(id, history);
                close();
            }
        }
    }

    public final void updatePlayers() {
        players.keySet().forEach(this::updatePlayer);
    }

    public final void updatePlayer(UUID playerId) {
        if (ended || !players.containsKey(playerId)) {
            return;
        }
        send(playerId, ServerGameMessages.ServerGameMessage.newBuilder()
                .setUpdateGameStateP(ServerGameMessages.UpdateGameStateP.newBuilder()
                        .setGame(toGameState(playerId, false))));
    }

    public Types.GameStateP.Builder toGameState(UUID playerId, boolean noAction) {
        Types.GameStateP.Builder b =
                Types.GameStateP.newBuilder()
                        .setId(id.toString())
                        .setPlayer(getPlayer(playerId).toPlayerMessage(true))
                        .setOpponent(getPlayer(this.getOpponentId(playerId)).toPlayerMessage(false))
                        .setTurnPlayer(turnPlayer.toString())
                        .setActivePlayer(activePlayer.toString())
                        .setPhase(phase);
        for (Card card : stack) {
            b.addStack(card.toCardMessage());
        }


        for (UUID attacker : attackers) {
            b.addAttackers(attacker.toString());
        }

        for (UUID blocker : blockers) {
            b.addBlockers(blocker.toString());
        }

        return b;
    }

    public Types.GameStateP.Builder toGameState(String username) {
        return toGameState(getPlayerId(username), false);
    }
}
