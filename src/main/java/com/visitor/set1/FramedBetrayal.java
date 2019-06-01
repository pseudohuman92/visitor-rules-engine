/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.card.types.Ritual;
import com.visitor.card.types.Spell;
import com.visitor.card.types.Tome;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import static com.visitor.protocol.Types.Knowledge.RED;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class FramedBetrayal extends Ritual{

    
    public FramedBetrayal(String owner) {
        super("Framed Betrayal", 4, new Hashmap(BLACK, 2), 
        "Target Ally's loyalty becomes 0", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) &&
                game.hasInstancesIn(controller, Ally.class, "both play", 1);
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, "both play", Predicates::isAlly, 1, false);
        super.play(game);
    }
    
    @Override
    public void resolveEffect (Game game){
        if(game.isIn(controller, targets.get(0), "both play")){
            ((Ally)game.getCard(targets.get(0))).loyalty = 0;
        }
    }
}

