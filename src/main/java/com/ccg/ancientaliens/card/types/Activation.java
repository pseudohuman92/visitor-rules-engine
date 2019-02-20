/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.card.types;

import com.ccg.ancientaliens.game.Game;
import helpers.Hashmap;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 *
 * @author pseudo
 */
public class Activation extends Card {
    
    BiConsumer<Game, Card> effect;
    
    public Activation (String name, String owner, String text, ArrayList<UUID> data, BiConsumer<Game, Card> effect){
        super(name, 0, new Hashmap<>(), text, owner);
        this.effect = effect;
        supplementaryData = data;
    }
    
    @Override
    public boolean canPlay(Game game) { return false; }

    @Override
    public void resolve(Game game) { effect.accept(game, this); }
    
}
