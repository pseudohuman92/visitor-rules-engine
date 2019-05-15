/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.red;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 *
 * @author pseudo
 */
public class RI04 extends Item {
    
    int x;
    
    public RI04 (String owner){
        super("RI04", 1, new Hashmap(RED, 1), 
            "Charge X.\n" +
            "\n" +
            "Dischage 1, Activate:\n" +
            "  Opponent purges 2.", owner);
    }
    
    @Override
    public void play (Game game) {
        x = game.selectX(controller, game.getEnergy(controller) - 1);
        game.spendEnergy(controller, x);
        text = "Charge "+x+".\n" +
            "\n" +
            "Dischage 1, Activate:\n" +
            "  Opponent purges 2.";
        super.play(game);
    }
    
    @Override
    public void resolve (Game game) {
        super.resolve(game);
        addCounters(CHARGE, x);
        text = "Charge X.\n" +
            "\n" +
            "Dischage 1, Activate:\n" +
            "  Opponent purges 2.";
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && counters.getOrDefault(CHARGE, 0) > 0;
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        removeCounters(CHARGE, 1);
        game.addToStack(new Activation (controller,
            game.getOpponentName(controller)+" purges 2.",
            (x) -> {
                game.damagePlayer(game.getOpponentName(controller), 2);
            })
        );
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage().setCost("X+1");
    }
}
