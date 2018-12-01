/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.tomes;

import cards.Tome;
import client.Client;
import enums.Knowledge;
import helpers.Hashmap;
import java.util.HashMap;

/**
 *
 * @author pseudo
 */
public class TomeBR extends Tome {
    
    public TomeBR(String owner) {
        super("Tome BR", "Gain {B} {R}", "tome.png", owner);
    }

    @Override
    public void playAsSource(Client client) {
        Hashmap<Knowledge, Integer> knl = new Hashmap<>();
        knl.put(Knowledge.BLACK, 1);
        knl.put(Knowledge.RED, 1);
        client.playSource(this, knl);
    }
    
}
