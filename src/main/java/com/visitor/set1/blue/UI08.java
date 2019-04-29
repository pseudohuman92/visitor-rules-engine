/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.blue;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.card.types.Junk;
import com.visitor.game.Game;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Counter.CHARGE;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import com.visitor.set1.additional.AI03;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class UI08 extends Item {

    
    UUID target;
    
    public UI08 (String owner){
        super("UI08", 3, new Hashmap(BLUE, 1), 
                "Discard a Junk: Charge 1.\n" +
                "Sacrifice an Item: Charge 1. \n" +
                "If it has 3 charges, transform ~ into AI03", owner);
    }
    
    public UI08 (AI03 c){
        super("UI08", 3, new Hashmap(BLUE, 1), 
                "Discard a Junk: Charge 1.\n" +
                "Sacrifice an Item: Charge 1. \n" +
                "If it has 3 charges, transform ~ into AI03", c.controller);
        copyPropertiesFrom(c);
    }

    @Override
    public boolean canActivate(Game game) {
        return game.hasInstancesIn(controller, Junk.class, "hand", 1) 
            || game.hasInstancesIn(controller, Item.class, "play", 1);
    }
    
    @Override
    public void activate(Game game) {
        Arraylist<Card> choices = new Arraylist<>();
        if (game.hasInstancesIn(controller, Junk.class, "hand", 1)){
            choices.add(new Activation(controller, "Discard a Junk: Charge 1.",
            (x1) -> {
                target = game.selectFromZone(controller, "hand", c -> {return c instanceof Junk;}, 1, false).get(0);
                game.discard(controller, target);
                game.addToStack(new Activation(controller, "Charge 1",
                    (x2) -> {
                        addCounters(CHARGE, 1);
                        if (counters.get(CHARGE) >= 3){
                            game.replaceWith(this, new AI03(this));
                        }
                    },
                new Arraylist<>(id)));
            }));
        }
        if (game.hasInstancesIn(controller, Item.class, "play", 1)){
            choices.add(new Activation(controller, "Sacrifice an Item: Charge 1.",
            (x1) -> {
                target = game.selectFromZone(controller, "play", c -> {return c instanceof Item;}, 1, false).get(0);
                game.destroy(target);
                game.addToStack(new Activation(controller, "Charge 1",
                    (x2) -> {
                        addCounters(CHARGE, 1);
                        if (counters.get(CHARGE) >= 3){
                            game.replaceWith(this, new AI03(this));
                        }
                    },
                new Arraylist<>(id)));
            }));
        }
        Arraylist<UUID> selection = game.selectFromList(controller, choices, c->{return true;}, 1, false);
        UUIDHelper.getInList(choices, selection).get(0).resolve(game);
    }
}