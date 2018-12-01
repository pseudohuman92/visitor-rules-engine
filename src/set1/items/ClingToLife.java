/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items;

import cards.Activation;
import cards.Item;
import enums.Knowledge;
import game.Game;

import helpers.Hashmap;
import set1.items.activations.ClingToLifeDrawActivation;
import set1.items.activations.ClingToLifeStartTrigger;

/**
 *
 * @author pseudo
 */
public class ClingToLife extends Item {
    public ClingToLife (String owner){
        super("Cling to Life", 2, new Hashmap(Knowledge.BLACK, 2), 
                "When Cling to Life enters play, lose 2 sanity and draw a card.<br><br>" +
"At the beginning of your turn, if you have 5 or less sanity,"
                        + "add a survival counter to Cling to life. "
                        + "If cling to life has 5 or more survival counters on it, you win the game.", 
                "item.png", owner);
        values = new int[0];
    }

    @Override
    public void resolve(Game game) {
        super.resolve(game);
        game.addToStack(new ClingToLifeDrawActivation(this));
    }
    
    @Override
    public Activation getPlayerStartTrigger() {
        return new ClingToLifeStartTrigger(this);
    }
}
