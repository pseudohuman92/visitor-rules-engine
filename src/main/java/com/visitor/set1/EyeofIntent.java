/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.card.types.Activation;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class EyeofIntent extends Item {
    
    public EyeofIntent (String owner){
        super("Eye of Intent", 1, new Hashmap(BLACK, 1), 
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
        game.addToStack(new Activation(this,
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
