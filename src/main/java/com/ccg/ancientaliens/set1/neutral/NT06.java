/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.neutral;

import com.ccg.ancientaliens.card.types.Tome;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;

/**
 *
 * @author pseudo
 */
public class NT06 extends Tome {

    public NT06(String owner) {
        super("NT06", "Study: Gain RR", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(RED, 2);
    }
    
}
