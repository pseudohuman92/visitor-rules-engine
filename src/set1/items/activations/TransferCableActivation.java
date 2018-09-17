/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items.activations;

import cards.Activation;
import game.Game;
import cards.Item;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class TransferCableActivation extends Activation{
    
    public TransferCableActivation (Item owner){
        super(owner, "Charge target item.");
    }

    @Override
    public void resolve(Game game) {
        game.charge((UUID) creator.supplimentaryData.get(1));
    }
}
