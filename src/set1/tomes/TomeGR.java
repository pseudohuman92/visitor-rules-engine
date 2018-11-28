/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.tomes;

import cards.Tome;
import client.Client;
import enums.Knowledge;
import java.util.HashMap;

/**
 *
 * @author pseudo
 */
public class TomeGR extends Tome {
    
    public TomeGR(String owner) {
        super("Tome GR", "Gain {G} {R}", "tome.png", owner);
    }

    @Override
    public void playAsSource(Client client) {
        HashMap<Knowledge, Integer> knl = new HashMap<>();
        knl.put(Knowledge.GREEN, 1);
        knl.put(Knowledge.RED, 1);
        client.playSource(this, knl);
    }
    
}
