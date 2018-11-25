/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items.activations;

import cards.Activation;
import game.Game;
import cards.Item;

/**
 *
 * @author pseudo
 */
public class ClingToLifeDrawActivation extends Activation{
    
    public ClingToLifeDrawActivation (Item owner){
        super(owner, "Lose 2 life and draw a card.");
    }

    @Override
    public void resolve(Game game) {
        game.players.get(owner).life -=2;
        game.players.get(owner).draw(1);
    }
}
