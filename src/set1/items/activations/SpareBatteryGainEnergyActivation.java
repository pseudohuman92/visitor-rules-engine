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
import game.Player;

/**
 *
 * @author pseudo
 */
public class SpareBatteryGainEnergyActivation extends Activation{
    
    public SpareBatteryGainEnergyActivation (Item owner){
        super(owner, "Gain "+ owner.counters.get(Counter.CHARGE)+" energy.");
    }

    @Override
    public void resolve(Game game) {
        Player player = game.players.get(creator.owner);
        Integer count = creator.counters.removeAndGet(Counter.CHARGE);
        if (count != null){
            player.energy += count;
        }
    }
}
