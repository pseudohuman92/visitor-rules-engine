/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items.activations;

import cards.Activation;
import game.Game;
import cards.Item;
import java.util.ArrayList;

/**
 *
 * @author pseudo
 */
public class EmpBombActivation extends Activation{
    
    public EmpBombActivation (Item owner){
        super(owner, "Destroy all items.");
    }

    @Override
    public void resolve(Game game) {
        game.players.values().forEach(player -> {
            player.discardPile.addAll(player.items);
            player.items = new ArrayList<>();
        });
    }
}
