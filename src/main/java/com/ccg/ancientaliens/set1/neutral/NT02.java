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
import static com.ccg.ancientaliens.protocol.Types.Knowledge.RED;

/**
 *
 * @author pseudo
 */
public class NT02 extends Tome {

    public NT02(String owner) {
        super("NT02", "Study: Gain BR", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(BLACK, 1).putIn(RED, 1);
    }
    
}
