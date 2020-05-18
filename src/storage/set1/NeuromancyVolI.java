/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;

import com.visitor.card.types.Tome;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

import static com.visitor.protocol.Types.Knowledge.BLACK;
import static com.visitor.protocol.Types.Knowledge.BLUE;

/**
 * @author pseudo
 */
public class NeuromancyVolI extends Tome {

    public NeuromancyVolI(String owner) {
        super("Neuromancy Vol. I", "Study: Gain BU", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(BLACK, 1).putIn(BLUE, 1);
    }

}
