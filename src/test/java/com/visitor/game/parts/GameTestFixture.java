package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.game.Player;
import com.visitor.protocol.Types;

import java.util.UUID;

/** Real engine objects with fixed initiative and clocks stopped between actions. */
public final class GameTestFixture implements AutoCloseable {
    public final Game game;
    public final Player player;
    public final Player opponent;

    public GameTestFixture() {
        this(new Game());
    }

    public GameTestFixture(Game game) {
        this.game = game;
        player = new Player(game, "player", new String[]{"20;base2.Stormtrooper"});
        opponent = new Player(game, "opponent", new String[]{"20;base2.Stormtrooper"});
        game.addPlayers(player, opponent);
        player.clock.pause();
        opponent.clock.pause();
        setPhase(Types.Phase.MAIN_BEFORE, player.getId(), player.getId());
    }

    public void setPhase(Types.Phase phase, UUID turn, UUID active) {
        game.phase = phase;
        game.turnPlayer = turn;
        game.activePlayer = active;
        game.passCount = 0;
    }

    public <T extends Card> T hand(T card, Player owner) {
        card.zone = Base.Zone.Hand;
        owner.hand.add(card);
        return card;
    }

    public <T extends Card> T play(T card, Player owner) {
        card.zone = Base.Zone.Play;
        owner.playArea.add(card);
        return card;
    }

    public String snapshot() {
        return view(player) + view(opponent)
                + "passes=" + game.passCount + ";events=" + game.eventQueue.size();
    }

    private String view(Player player) {
        Types.GameStateP.Builder state = game.toGameState(player.getId(), false);
        // Wall-clock ticks are independent of whether a command changes the rules state.
        state.getPlayerBuilder().clearTime();
        state.getOpponentBuilder().clearTime();
        return state.build().toString();
    }

    @Override
    public void close() throws InterruptedException {
        game.close();
        player.clock.interrupt();
        opponent.clock.interrupt();
        player.clock.join(1000);
        opponent.clock.join(1000);
    }
}
