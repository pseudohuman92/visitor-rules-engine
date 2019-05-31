/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;


import com.visitor.card.types.Ability;
import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class SaulgolathEnforcer extends Ally {

    public SaulgolathEnforcer(String owner){
        super ("Sa'ulgolath Enforcer", 1, new Hashmap(RED, 1),
            "Pay 2 life, Activate: +2 Loyalty\n"+
            "-X Loyalty, Activate: \n"+
            "  Favor 1 - Deal 2X damage to opponent.", 5,
            owner);
    }

    @Override
    public void activate(Game game) {
        Arraylist<Card> choices = new Arraylist<>();
        choices.add(new Ability(this, "Pay 2 life, Activate: +2 Loyalty",
        (x1) -> {
            depleted = true;
            game.payLife(controller, 2);
            game.addToStack(new Ability(this, "+2 Loyalty",
            (x2) -> {
                loyalty +=2;
            }));
        }));
        if (loyalty >= 1){
            choices.add(new Ability(this, 
                    "-X Loyalty, Activate: \n"+
                    "  Favor 1 - Deal 2X damage to opponent.",
            (x1) -> {
                int x = game.selectX(controller, loyalty);
                depleted = true;
                loyalty -=x;
                favor = 1;
                favorAbility =  new Ability(this, "Deal " + 2*x +" damage to opponent.",
                    (x2) -> {
                        game.dealDamage(id, game.getOpponentUid(controller), 2*x);
                    },
                new Arraylist<>(game.getOpponentUid(controller)));
            }));
        }
        Arraylist<UUID> selection = game.selectFromList(controller, choices, Predicates::any, 1, false);
        UUIDHelper.getInList(choices, selection).get(0).resolve(game);
    }
    
}

