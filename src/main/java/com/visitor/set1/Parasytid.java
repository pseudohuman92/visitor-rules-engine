/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Ability;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Parasytid extends Item {
    
    public Parasytid (String owner){
        super("Parasytid", 2, new Hashmap(BLACK, 2), 
                "3, Sacrifice ~, Activate: Possess target item.", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return !depleted;
    }

    @Override
    public void activate(Game game) {
        game.deplete(id);
        game.spendEnergy(controller, 3);
        Arraylist<UUID> selected = game.selectFromZone(controller, BOTH_PLAY, Predicates::isItem, 1, false);
        game.destroy(id);
        game.addToStack(new Ability(this,
            "Possess target item",
            (x) -> {
                if (game.isIn(controller, selected.get(0), BOTH_PLAY)) {
                    game.possessTo(controller, selected.get(0), PLAY);
                }
            }, selected));
    }
}
