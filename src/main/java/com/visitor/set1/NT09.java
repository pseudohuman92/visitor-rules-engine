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
public class NT09 extends Tome {

    public NT09(String owner) {
        super("NT09", "Study: Gain GR", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(GREEN, 1).putIn(RED, 1);
    }
    
}
