/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items.activations;

import set1.items.*;
import cards.Activation;
import enums.Counter;
import game.Game;
import cards.Item;

/**
 *
 * @author pseudo
 */
public class DuplicatingCubeActivation extends Activation{
    
    public DuplicatingCubeActivation (Item owner){
        super(owner, "Duplicate this.");
    }

    @Override
    public void resolve(Game game) {
        addCounters(Counter.CHARGE, values[0]);
        values[0] += values[1];
        game.players.get(creator.owner).items.add(((DuplicatingCube)creator).duplicate());
    }
}
