package com.visitor.game.parts;

import com.visitor.game.Card;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.ServerGameMessages;
import com.visitor.protocol.Types;
import com.visitor.server.GameEndpointInterface;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.visitor.game.parts.GameBasePart.Zone.Both_Play;
import static com.visitor.helpers.UUIDHelper.toUUIDList;
import static com.visitor.protocol.Types.SelectFromType.LIST;
import static com.visitor.server.GeneralEndpoint.gameServer;
import static java.lang.System.out;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class GameMessagingPart extends GameAccessorPart {
    /**
     * Client Prompt Methods
     * When you need client to do something.
     */
    //// Helpers
    private void send (UUID playerId, ServerGameMessages.ServerGameMessage.Builder builder) {
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

    private Arraylist<UUID> selectFrom (UUID playerId, Types.SelectFromType type, Arraylist<com.visitor.game.Card> candidates, Arraylist<UUID> canSelect, Arraylist<UUID> canSelectPlayers, int count, boolean upTo, String message) {
        ServerGameMessages.SelectFrom.Builder b = ServerGameMessages.SelectFrom.newBuilder()
                .addAllSelectable(canSelect.transformToStringList())
                .addAllSelectable(canSelectPlayers.transformToStringList())
                .addAllCandidates(candidates.transform(c -> c.toCardMessage().build()))
                .setMessageType(type)
                .setSelectionCount(count)
                .setUpTo(upTo)
                .setMessage(message)
                .setGame(toGameState(playerId, true));
        try {
            send(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setSelectFrom(b));
            String[] l = (String[]) response.take();
            return toUUIDList(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

    Arraylist<Types.AttackerAssignment> selectAttackers(UUID playerId) {

        List<String> attackers = getZone(playerId, Game.Zone.Play).parallelStream()
                .filter(com.visitor.game.Card::canAttack)
                .map(u -> u.id.toString()).collect(Collectors.toList());

        if (attackers.isEmpty()) {
            return (new Arraylist<>());
        }
        Arraylist<String> targets = new Arraylist<>(getOpponentId(playerId).toString());
        List<String> allies = getZone(this.getOpponentId(playerId), Game.Zone.Play).parallelStream()
                .filter(Predicates::isAlly)
                .map(u -> u.id.toString()).collect(Collectors.toList());
        targets.addAll(allies);
        List<Types.Attacker> attackerList = attackers.parallelStream()
                .map(a -> Types.Attacker.newBuilder()
                        .setAttackerId(a)
                        .addAllPossibleAttackTargets(targets).build())
                .collect(Collectors.toList());
        out.println("Sending Select Attackers Message to " + playerId);
        ServerGameMessages.SelectAttackers.Builder b = ServerGameMessages.SelectAttackers.newBuilder()
                .addAllPossibleAttackers(attackerList)
                .setGame(toGameState(playerId, true));
        try {
            send(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setSelectAttackers(b));
            Types.AttackerAssignment[] l = (Types.AttackerAssignment[]) response.take();
            return new Arraylist<>(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

    Arraylist<Types.BlockerAssignment> selectBlockers(UUID playerId) {

        List<com.visitor.game.Card> potentialBlockers =
                getZone(playerId, Game.Zone.Play)
                        .parallelStream()
                        .filter(com.visitor.game.Card::canBlock)
                        .collect(Collectors.toList());

        if (potentialBlockers.isEmpty()) {
            return (new Arraylist<>());
        }

        Arraylist<Types.Blocker> blockers = new Arraylist<>();
        potentialBlockers.forEach(pb -> {
            List<String> targets = attackers.parallelStream()
                    .filter(a -> pb.canBlock(getCard(a)))
                    .map(u -> getCard(u).id.toString())
                    .collect(Collectors.toList());
            if (!targets.isEmpty()) {
                blockers.add(Types.Blocker.newBuilder()
                        .setBlockerId(pb.id.toString())
                        .addAllPossibleBlockTargets(targets)
                        .build());
            }
        });

        if (blockers.isEmpty()) {
            return (new Arraylist<>());
        }

        out.println("Sending Select Blockers Message to " + playerId);
        ServerGameMessages.SelectBlockers.Builder b = ServerGameMessages.SelectBlockers.newBuilder()
                .addAllPossibleBlockers(blockers)
                .setGame(toGameState(playerId, true));
        try {
            send(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setSelectBlockers(b));
            Types.BlockerAssignment[] l = (Types.BlockerAssignment[]) response.take();
            return new Arraylist<>(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

    Arraylist<Types.DamageAssignment> assignDamage(UUID playerId, UUID id, Arraylist<UUID> possibleTargets, int damage, boolean trample) {
        out.println("Sending Assign Damage Message to " + playerId);
        ServerGameMessages.AssignDamage.Builder b = ServerGameMessages.AssignDamage.newBuilder()
                .setDamageSource(id.toString())
                .addAllPossibleTargets(possibleTargets.transformToStringList())
                .setTotalDamage(damage)
                .setTrample(trample)
                .setGame(toGameState(playerId, true));
        try {
            send(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setAssignDamage(b));
            Types.DamageAssignment[] l = (Types.DamageAssignment[]) response.take();
            return new Arraylist<>(l);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

    //// Prompters
    public Arraylist<UUID> selectFromZone (UUID playerId, Game.Zone zone, Predicate<Card> validTarget, int count, boolean upTo, String message) {
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(playerId, zone).parallelStream()
                .filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
        return selectFrom(playerId, getZoneLabel(zone), getZone(playerId, zone), canSelect, new Arraylist<>(), count, upTo, message);
    }

    public Arraylist<UUID> selectFromZoneWithPlayers (UUID playerId, Game.Zone zone, Predicate<com.visitor.game.Card> validTarget, Predicate<com.visitor.game.Player> validPlayer, int count, boolean upTo, String message) {
        Arraylist<UUID> canSelect = new Arraylist<>(getZone(playerId, zone).parallelStream()
                .filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(c -> c.id).collect(Collectors.toList()));
        return selectFrom(playerId, getZoneLabel(zone), getZone(playerId, zone), canSelect, canSelectPlayers, count, upTo, message);
    }

    public Arraylist<UUID> selectFromList (UUID playerId, Arraylist<com.visitor.game.Card> candidates, Predicate<com.visitor.game.Card> validTarget, int count, boolean upTo, String message) {
        if (message == null || message.equals("")){
            message = "Select " + (upTo? "up to " : "") + count;
        }
        Arraylist<UUID> canSelect = new Arraylist<>(candidates.parallelStream()
                .filter(validTarget).map(c -> c.id).collect(Collectors.toList()));
        return selectFrom(playerId, LIST, candidates, canSelect, new Arraylist<>(), count, upTo, message);
    }

    public Arraylist<UUID> selectPlayers (UUID playerId, Predicate<com.visitor.game.Player> validPlayer, int count, boolean upTo) {
        Arraylist<UUID> canSelectPlayers = new Arraylist<>(players.values().parallelStream()
                .filter(validPlayer).map(c -> c.id).collect(Collectors.toList()));
        String message = "Select " + (upTo ? " up to " : "") + count + " player" + (count > 1 ? "s." : ".");
        return selectFrom(playerId, getZoneLabel(Game.Zone.Play), new Arraylist<>(), new Arraylist<>(), canSelectPlayers, count, upTo, message);
    }

    public Arraylist<UUID> selectDamageTargetsConditional (UUID playerId, Predicate<com.visitor.game.Card> validTarget, Predicate<com.visitor.game.Player> validPlayer, int count, boolean upTo, String message) {
        return selectFromZoneWithPlayers(playerId, Both_Play, validTarget, validPlayer, count, upTo, message);
    }

    public Arraylist<UUID> selectDamageTargets (UUID playerId, int count, boolean upTo, String message) {
        return selectFromZoneWithPlayers(playerId, Both_Play, Predicates::isDamageable, Predicates::any, count, upTo, message);
    }

    public int selectX (UUID playerId, int maxX) {
        if (maxX == 0) {
            return maxX;
        }
        ServerGameMessages.SelectXValue.Builder b = ServerGameMessages.SelectXValue.newBuilder()
                .setMaxXValue(maxX)
                .setGame(toGameState(playerId, true));
        try {
            send(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setSelectXValue(b));

            return (int) response.take();
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return 0;
    }

    public CounterMap<Types.Knowledge> selectKnowledge (UUID playerId, Set<Types.Knowledge> knowledgeSet) {
        ServerGameMessages.SelectKnowledge.Builder b = ServerGameMessages.SelectKnowledge.newBuilder()
                .addAllKnowledgeList(knowledgeSet)
                .setGame(toGameState(playerId, true));
        out.println(b.build());
        try {
            send(playerId, ServerGameMessages.ServerGameMessage.newBuilder().setSelectKnowledge(b));
            return new CounterMap<>((Types.Knowledge) response.take(), 1);
        } catch (InterruptedException ex) {
            getLogger(Game.class.getName()).log(SEVERE, null, ex);
        }
        return new CounterMap<>();
    }

    public void gameEnd (UUID playerId, boolean win) {
        send(playerId, ServerGameMessages.ServerGameMessage.newBuilder()
                .setGameEnd(ServerGameMessages.GameEnd.newBuilder()
                        .setGame(toGameState(playerId, true))
                        .setWin(win)));
        send(getOpponentId(playerId), ServerGameMessages.ServerGameMessage.newBuilder()
                .setGameEnd(ServerGameMessages.GameEnd.newBuilder()
                        .setGame(toGameState(getOpponentId(playerId), true))
                        .setWin(!win)));
        connections.forEach((s, c) -> c.close());
        connections = new Hashmap<UUID, GameEndpointInterface>();
        gameServer.removeGame(id, history);
    }

    public final void updatePlayers () {
        players.forEach((name, player) -> send(name, ServerGameMessages.ServerGameMessage.newBuilder()
                .setUpdateGameState(ServerGameMessages.UpdateGameState.newBuilder()
                        .setGame(toGameState(name, false)))));
    }

    public Types.GameState.Builder toGameState (UUID playerId, boolean noAction) {
        Types.GameState.Builder b =
                Types.GameState.newBuilder()
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

        if(noAction)
            return b;

        players.forEach((s, p) -> {
            if (playerId.equals(s) && isPlayerActive(s)) {
                p.hand.forEach(c -> {
                    if (c.canPlay(true)) {
                        b.addCanPlay(c.id.toString());
                    }
                    if (c.canStudy()) {
                        b.addCanStudy(c.id.toString());
                    }
                });
                p.playArea.forEach(c -> {
                    if (c.canActivate()) {
                        b.addCanActivate(c.id.toString());
                    }
                });
            }
        });
        return b;
    }

    public Types.GameState.Builder toGameState (String username) {
        return toGameState(getPlayerId(username), false);
    }
}
