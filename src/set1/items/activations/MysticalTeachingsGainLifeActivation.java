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
public class MysticalTeachingsGainLifeActivation extends Activation{
    
    public MysticalTeachingsGainLifeActivation (Item owner){
        super(owner, "Gain 5 life.");
    }

    @Override
    public void resolve(Game game) {
        game.players.get(owner).life +=5;
    }
}
