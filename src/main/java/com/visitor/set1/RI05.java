/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class RI05 extends Item{
    
    public RI05 (String owner){
        super("RI05", 2, new Hashmap(RED, 3), 
                "\"Discard a card: \n" +
                "  Charge 1. \n" +
                "\n" +
                "Discharge 1, Activate: \n" +
                "  Deal 2 damage", owner);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasCardsIn(controller, "hand", 1)
                || (!depleted && counters.getOrDefault(CHARGE, 0) > 0);
    }
    
    @Override
    public void activate(Game game) {
        Arraylist<Card> choices = new Arraylist<>();
        if (game.hasCardsIn(controller, "hand", 1)){
            choices.add(new Activation(controller, "Discard a card: Charge 1.",
            (x1) -> {
                game.discard(controller, 1);
                game.addToStack(new Activation(controller, "Charge 1",
                    (x2) -> {
                        addCounters(CHARGE, 1);
                    },
                new Arraylist<>(id)));
            }));
        }
        if (!depleted && counters.getOrDefault(CHARGE, 0) > 0){
            choices.add(new Activation(controller, "Discharge 1, Activate: Deal 2 damage",
            (x1) -> {
                removeCounters(CHARGE, 1);
                game.deplete(id);
                UUID target = game.selectDamageTargets(controller, 1, false).get(0);
                game.addToStack(new Activation (controller,
                    "Deal 2 damage",
                    (x) -> {
                        game.dealDamage(target, 2);
                    })
                );
            }));
        }
        Arraylist<UUID> selection = game.selectFromList(controller, choices, c->{return true;}, 1, false);
        UUIDHelper.getInList(choices, selection).get(0).resolve(game);
    }

    
}
