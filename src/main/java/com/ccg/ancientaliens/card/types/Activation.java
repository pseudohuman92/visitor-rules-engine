/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.helpers.Arraylist;
import com.ccg.ancientaliens.helpers.Hashmap;
import com.ccg.ancientaliens.protocol.Types;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @author pseudo
 */
public class Activation extends Card {
    
    Consumer<Void> effect;
    
    public Activation (String owner, String text, Consumer<Void> effect){
        super("", 0, new Hashmap<>(), text, owner);
        this.effect = effect;
    }
    
    public Activation (String owner, String text, Consumer<Void> effect, Arraylist<UUID> targets){
        super("", 0, new Hashmap<>(), text, owner);
        this.effect = effect;
        this.targets = targets;
    }
    
    @Override
    public boolean canPlay(Game game) { return false; }

    @Override
    public void resolve(Game game) { effect.accept(null); }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Activation");
    }
    
}
