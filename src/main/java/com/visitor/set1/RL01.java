/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;


import com.visitor.card.types.Activation;
import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class RL01 extends Ally {

    public RL01(String owner){
        super ("RL01", 1, new Hashmap(RED, 1),
            "Pay 2 life, Activate: +2 Loyalty\n"+
            "-X Loyalty, Activate: \n"+
            "  Favor 1 - Deal 2X damage to opponent.", 5,
            owner);
    }

    @Override
    public void activate(Game game) {
        Arraylist<Card> choices = new Arraylist<>();
        choices.add(new Activation(controller, "Pay 2 life, Activate: +2 Loyalty",
        (x1) -> {
            depleted = true;
            game.payLife(controller, 2);
            game.addToStack(new Activation(controller, "+2 Loyalty",
            (x2) -> {
                loyalty +=2;
            },
            new Arraylist<>(id)));
        }));
        if (loyalty >= 1){
            choices.add(new Activation(controller, 
                    "-X Loyalty, Activate: \n"+
                    "  Favor 1 - Deal 2X damage to opponent.",
            (x1) -> {
                int x = game.selectX(controller, loyalty);
                depleted = true;
                loyalty -=x;
                favor = 1;
                favorAbility =  new Activation(controller, "Deal " + 2*x +" damage to opponent.",
                    (x2) -> {
                        game.dealDamage(game.getOpponentId(controller), 2*x);
                    },
                new Arraylist<>(id).putIn(game.getOpponentId(controller)));
            }));
        }
        Arraylist<UUID> selection = game.selectFromList(controller, choices, c->{return true;}, 1, false);
        UUIDHelper.getInList(choices, selection).get(0).resolve(game);
    }
    
}

