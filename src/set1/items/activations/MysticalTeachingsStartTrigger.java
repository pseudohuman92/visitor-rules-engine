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
public class MysticalTeachingsStartTrigger extends Activation{
    
    public MysticalTeachingsStartTrigger (Item owner){
        super(owner, "If you have more than 30 sanity, add a transcendence counter on Mystical Teachings. "
                        + "If Mystical Teachings has 5 or more transcendence counters on it, you win the game.");
    }

    @Override
    public void resolve(Game game) {
        if (game.players.get(owner).life > 30){
            Integer i = creator.counters.get(Counter.TRANSENDENCE);
            if (i== null){
                creator.counters.put(Counter.TRANSENDENCE, 1);
            } else {
                creator.counters.put(Counter.TRANSENDENCE, i+1);
                if (i >= 4){
                    game.win(owner);
                }
            }
        }
    }
}
