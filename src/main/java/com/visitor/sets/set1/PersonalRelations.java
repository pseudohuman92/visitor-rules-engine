/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;

import com.visitor.card.types.Tome;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public class PersonalRelations extends Tome {

    public PersonalRelations(String owner) {
        super("Personal Relations", "Study: Gain GG", owner);
    }

    @Override
    public Hashmap<Types.Knowledge, Integer> getKnowledgeType() {
        return new Hashmap(GREEN, 2);
    }
    
}
