/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.card.properties.Activatable;
import com.ccg.ancientaliens.card.properties.Triggering;
import com.ccg.ancientaliens.game.Event;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;

/**
 *
 * @author pseudo
 */
public abstract class Ally extends Card implements Activatable, Triggering {
    
    int favor;
    Activation favorAbility;
    int loyalty;

    public Ally(String name, int cost, 
            Hashmap<Types.Knowledge, Integer> knowledge, 
            String text, String owner) {
        super(name, cost, knowledge, text, owner);
        favor = 0;
        loyalty = 0;
        favorAbility = null;
    }
    
    @Override
    public void resolve(Game game) {
        game.putTo(controller, this, "play");
        game.registerTriggeringCard(controller, this);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return game.hasEnergy(controller, cost)
               && game.hasKnowledge(controller, knowledge)
               && game.canPlaySlow(controller);
    }
    
    @Override
    public void checkEvent(Game game, Event event){
        if (event.label.equals("Begin Turn") && favor > 0){
            favor--;
            if (favor == 0){
                game.addToStack(favorAbility);
                favorAbility = null;
            }
        }
    }
    
    @Override
    public void ready(){
        if (favor == 0){
            depleted = false;
        }
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Ally");
    }
    
}
