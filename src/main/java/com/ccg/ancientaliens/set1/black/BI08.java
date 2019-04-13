/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.black;

import com.ccg.ancientaliens.card.types.Spell;
import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class BI08 extends Item {
    
    public BI08 (String owner){
        super("BI08", 1, new Hashmap(BLACK, 1), 
                "1, Activate, Sacrifice ~: \n" +
                "  Look at opponent's hand and choose an spell from it. \n" +
                "  They discard it.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 1);
    }

    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 1);
        game.deplete(id);
        game.destroy(id);
        game.addToStack(new Activation (controller,
            "Look at "+game.getOpponentName(controller)+"'s hand and choose an spell from it. \n" +
            "They discard it.",
            (x) -> {
                Arraylist<UUID> selected = 
                        game.selectFromList(controller, 
                                game.getZone(game.getOpponentName(controller), "hand"), 
                        c->{return c instanceof Spell;}, 1, true);
                if(!selected.isEmpty()){
                    game.discard(game.getOpponentName(controller), selected.get(0));
                }
            })
        );
    }

}
