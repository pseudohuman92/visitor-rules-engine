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

import set1.items.activations.MysticalTeachingsGainLifeActivation;
import set1.items.activations.MysticalTeachingsStartTrigger;

/**
 *
 * @author pseudo
 */
public class MysticalTeachings extends Item {
    public MysticalTeachings (String owner){
        super("Mystical Teachings", 3, new Hashmap(Knowledge.WHITE, 2), 
                "When Mystical Teachings enters play, you gain 5 sanity.<br><br>" +
"At the start of your turn, if you have more than 30 sanity, add a transcendence counter on Mystical Teachings. "
                        + "If Mystical Teachings has 5 or more transcendence counters on it, you win the game.", 
                "item.png", owner);
        values = new int[0];
    }

    @Override
    public void resolve(Game game) {
        super.resolve(game);
        game.addToStack(new MysticalTeachingsGainLifeActivation(this));
    }
    
    public Activation getPlayerStartTrigger() {
        return new MysticalTeachingsStartTrigger(this);
    }
}
