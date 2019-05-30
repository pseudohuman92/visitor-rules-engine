/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;


import com.visitor.card.types.Activation;
import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.UUIDHelper;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class TheHighwayman extends Ally {

    public TheHighwayman(String owner){
        super ("The Highwayman", 2, new Hashmap(BLACK, 2),
            "3, Activate: +2 Loyalty\n"+
            "-1 Loyalty, Activate: \n"+
            "  Favor 2 - Draw the top item of opponent's deck.", 5,
            owner);
    }
        
    @Override
    public boolean canActivate(Game game){
        return super.canActivate(game) && 
                (game.hasEnergy(controller, 3) || loyalty >= 1); 
    }
    

    @Override
    public void activate(Game game) {
        Arraylist<Card> choices = new Arraylist<>();
        if (game.hasEnergy(controller, 3)){
            choices.add(new Activation(this, "Pay 3: +2 Loyalty",
            (x1) -> {
                depleted = true;
                game.spendEnergy(controller, 3);
                game.addToStack(new Activation(this, "+2 Loyalty",
                (x2) -> {
                    loyalty +=2;
                }));
            }));
        }
        if (loyalty >= 1){
            choices.add(new Activation(this, "-1 Loyalty: Favor 2:\n" +
                        "  Draw the top item of opponent's deck.",
            (x1) -> {
                depleted = true;
                loyalty -=1;
                favor = 2;
                favorAbility =  new Activation(this, "Draw the top item of opponent's deck.",
                    (x2) -> {
                        Card c = game.getPlayer(game.getOpponentName(controller))
                        .deck.extractInstanceFromTop(Item.class);
                        if(c != null){
                            c.controller = controller;
                            c.knowledge = new Hashmap<>();
                            game.putTo(c.controller, c, "hand");
                        }
                    });
            }));
        }
        Arraylist<UUID> selection = game.selectFromList(controller, choices, c->{return true;}, 1, false);
        UUIDHelper.getInList(choices, selection).get(0).resolve(game);
    }
    
}

