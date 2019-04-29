/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.visitor.set1.yellow;

import com.visitor.card.types.Activation;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;


/**
 *
 * @author pseudo
 */
public class YI03 extends Item {
    
    public YI03 (String owner){
        super("YI03", 3, new Hashmap(YELLOW, 3), 
                "\"2, Activate: \n" +
                "  Create and draw a YI01 or YI02.\"", owner);
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.hasEnergy(controller, 2);
    }
    
    @Override
    public void activate(Game game) {
        game.spendEnergy(controller, 2);
        game.addToStack(new Activation(controller, "Create and draw YI01 or YI02",
        (x) -> {
            Arraylist<Card> choices = new Arraylist<>();
            choices.add(new Activation(controller, "Create and draw YI01",
                (x1) -> {
                    game.putTo(controller, new YI01(controller), "hand");
            }));
            choices.add(new Activation(controller, "Create and draw YI02",
                (x1) -> {
                    game.putTo(controller, new YI02(controller), "hand");
            }));
            Arraylist<UUID> selection = game.selectFromList(controller, choices, c->{return true;}, 1, false);
            UUIDHelper.getInList(choices, selection).get(0).resolve(game);
        }));
    }
}
