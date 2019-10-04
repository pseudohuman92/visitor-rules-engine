/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Passive;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

/**
 *
 * @author pseudo
 */
public abstract class TriggeringPassive extends Passive implements Triggering {
    
    public TriggeringPassive(String name, int cost, Hashmap<Types.Knowledge, Integer> knowledge, String text, String owner) {
        super(name, cost, knowledge, text, owner);
    }
    
    @Override
    protected void afterResolve(Game game) {
        game.addTriggeringCard(controller, this);
    }
    
    @Override
    public void destroy(Game game) {
        super.destroy(game);
        game.removeTriggeringCard(this);
    }
    
    @Override
    public void sacrifice(Game game) {
        super.sacrifice(game);
        game.removeTriggeringCard(this);
    }
}
