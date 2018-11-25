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

/**
 *
 * @author pseudo
 */
public class SpareBatteryAddCounterActivation extends Activation{
    
    public SpareBatteryAddCounterActivation (Item owner){
        super(owner, "Place " + owner.supplimentaryData.get(1) + " charge counters.");
    }

    @Override
    public void resolve(Game game) {
        creator.counters.put(Counter.CHARGE, ((int)creator.supplimentaryData.get(1)));
    }
}
