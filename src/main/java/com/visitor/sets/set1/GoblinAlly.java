/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;


import com.visitor.card.types.helpers.Ability;
import com.visitor.card.types.Ally;
import com.visitor.card.types.Card;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.helpers.UUIDHelper.getInList;
import static com.visitor.protocol.Types.Knowledge.GREEN;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class GoblinAlly extends Ally {
    
    public GoblinAlly(String owner){
        super ("Goblin Ally", 0, new Hashmap(),
            "", 2,
            owner);
    }

    @Override
    public boolean canActivateAdditional(Game game){
        return false;
    }


    @Override
    public void activate(Game game) {}
    
}

