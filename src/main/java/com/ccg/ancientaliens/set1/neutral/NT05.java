/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.neutral;

import com.ccg.ancientaliens.card.types.Tome;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;

/**
 *
 * @author pseudo
 */
public class NT05 extends Tome {

    public NT05(String owner) {
        super("NT05", "Study: Gain UU", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(BLUE, 2);
    }
    
}
