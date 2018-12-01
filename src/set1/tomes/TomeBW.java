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
public class TomeBW extends Tome {
    
    public TomeBW(String owner) {
        super("Tome BW", "Gain {B} {W}", "tome.png", owner);
    }

    @Override
    public void playAsSource(Client client) {
        Hashmap<Knowledge, Integer> knl = new Hashmap<>();
        knl.put(Knowledge.BLACK, 1);
        knl.put(Knowledge.WHITE, 1);
        client.playSource(this, knl);
    }
    
}
