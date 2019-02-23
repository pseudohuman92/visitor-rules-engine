/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.properties.Transforming;
import com.ccg.ancientaliens.card.types.Activation;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.card.types.Junk;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.UUIDHelper;
import static com.ccg.ancientaliens.protocol.Types.Counter.CHARGE;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import com.ccg.ancientaliens.set1.additional.AI03;
import com.ccg.ancientaliens.helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class UI08 extends Item implements Transforming {

    
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
        return game.hasAnInstanceIn(controller, Junk.class, "hand") 
            || game.hasAnInstanceIn(controller, Item.class, "play");
    }
    
    @Override
    public void activate(Game game) {
        ArrayList<Card> choices = new ArrayList<>();
        if (game.hasAnInstanceIn(controller, Junk.class, "hand")){
            choices.add(new Activation(controller, "Discard a Junk: Charge 1.",
            (g, c) -> {
                target = g.selectFromHand(controller, c1 -> {return c1 instanceof Junk;}, 1).get(0);
                g.discard(controller, target);
                g.addToStack(new Activation(controller, "Charge 1",
                    (g1, c2) -> {
                        addCounters(CHARGE, 1);
                        if (counters.get(CHARGE) >= 3){
                            transform(g1);
                        }
                    }
                ));
            }));
        }
        if (game.hasAnInstanceIn(controller, Item.class, "play")){
            choices.add(new Activation(controller, "Sacrifice an Item: Charge 1.",
            (g, c) -> {
                target = g.selectFromPlay(controller, c1 -> {return c1.controller.equals(controller) && c1 instanceof Item;}, 1).get(0);
                g.destroy(target);
                g.addToStack(new Activation(controller, "Charge 1",
                    (g1, c2) -> {
                        addCounters(CHARGE, 1);
                        if (counters.get(CHARGE) >= 3){
                            transform(g1);
                        }
                    }
                ));
            }));
        }
        ArrayList<UUID> selection = game.selectFromList(controller, choices, c->{return true;}, 1);
        UUIDHelper.getInList(choices, selection).get(0).resolve(game);
    }

    @Override
    public void transform(Game game) {
        game.replaceWith(this, new AI03(this));
    }
}
