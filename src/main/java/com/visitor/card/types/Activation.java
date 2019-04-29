/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types;

import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
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
