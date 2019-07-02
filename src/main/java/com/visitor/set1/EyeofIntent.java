/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Asset;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.HAND;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class EyeofIntent extends Asset {
    
    public EyeofIntent (String owner){
        super("Eye of Intent", 1, new Hashmap(BLACK, 1), 
                "1, Activate, Sacrifice ~: \n" +
                "  Look at opponent's hand and choose an spell from it. \n" +
                "  They discard it.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return super.canActivate(game) && game.hasEnergy(controller, 1);
    }

    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 1);
        game.deplete(id);
        game.sacrifice(id);
        game.addToStack(new Ability(this,
            "Look at "+game.getOpponentName(controller)+"'s hand and choose an spell from it. \n" +
            "They discard it.",
            (x) -> {
                Arraylist<UUID> selected = 
                        game.selectFromList(controller, 
                                game.getZone(game.getOpponentName(controller), HAND), 
                        Predicates::isSpell, 1, true);
                if(!selected.isEmpty()){
                    game.discard(game.getOpponentName(controller), selected.get(0));
                }
            })
        );
    }

}
