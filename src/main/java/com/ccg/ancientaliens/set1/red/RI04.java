/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.red;

import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;
import static com.ccg.ancientaliens.protocol.Types.Counter.CHARGE;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;

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
                game.purge(game.getOpponentName(controller), 2);
            })
        );
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage().setCost("X+1");
    }
}
