/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items.activations;

import cards.Activation;
import game.Game;
import cards.Item;
import enums.Counter;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class ClingToLifeStartTrigger extends Activation{
    
    public ClingToLifeStartTrigger (Item owner){
        super(owner, "If you have 5 or less sanity, add a survival counter to Cling to life. "
                        + "If cling to life has 5 or more survival counters on it, you win the game.");
    }

    @Override
    public void resolve(Game game) {
        if (game.players.get(owner).life <= 5){
            Integer i = creator.counters.get(Counter.SURVIVAL);
            if (i== null){
                creator.counters.put(Counter.SURVIVAL, 1);
            } else {
                creator.counters.put(Counter.SURVIVAL, i+1);
                if (i >= 4){
                    game.win(owner);
                }
            }
        }
    }
}
