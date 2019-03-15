/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.neutral;

import com.ccg.ancientaliens.card.types.Tome;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLACK;

/**
 *
 * @author pseudo
 */
public class NT04 extends Tome {

    public NT04(String owner) {
        super("NT04", "Study: Gain BB", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(BLACK, 2);
    }
    
}
