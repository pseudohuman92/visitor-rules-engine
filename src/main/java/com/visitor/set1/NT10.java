/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Tome;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.GREEN;
import static com.visitor.protocol.Types.Knowledge.RED;

/**
 *
 * @author pseudo
 */
public class NT10 extends Tome {

    public NT10(String owner) {
        super("NT10", "Study: Gain GU", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(BLUE, 1).putIn(GREEN, 1);
    }
    
}
