/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items.activations;

import cards.Activation;
import game.Game;
import cards.Item;

import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class NanobombActivation extends Activation{
    
    public NanobombActivation (Item owner){
        super(owner, "Target player loses 1 sanity.");
    }

    @Override
    public void resolve(Game game) {
        creator.supplimentaryData.forEach(uuid -> game.getPlayer((UUID)uuid).life--);
    }
}
