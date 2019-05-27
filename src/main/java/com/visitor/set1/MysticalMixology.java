/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Tome;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import static com.visitor.protocol.Types.Knowledge.BLACK;

/**
 *
 * @author pseudo
 */
public class MysticalMixology extends Tome {

    public MysticalMixology(String owner) {
        super("Mystical Mixology", "Study: Gain BB", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(BLACK, 2);
    }
    
}
