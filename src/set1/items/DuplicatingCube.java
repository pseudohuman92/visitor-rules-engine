/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items;

import cards.Activation;
import game.ClientGame;
import enums.Counter;
import game.Game;
import cards.Card;
import enums.Type;
import cards.Item;
import java.awt.Color;
import java.util.HashMap;
import set1.items.activations.DuplicatingCubeActivation;

/**
 *
 * @author pseudo
 */
public class DuplicatingCube extends Item{
    
    public DuplicatingCube (String owner){
        super("Duplicating Cube", 0, new HashMap<>(), 
                "Exhaust: Add %s charge counters, increase this ability %s, then duplicate", 
                "item.png", owner);
        values = new int[2];
        values[0] = 1;
        values[1] = 1;
    }
    
    public DuplicatingCube (DuplicatingCube c){
        super(c);
    }
    
    @Override
    public void activate(Game game) {
        depleted = true;
        super.activate(game);
    }

    @Override
    public Activation getActivation() {
        return new DuplicatingCubeActivation (this);
    }
    
    @Override
    public boolean canActivate(ClientGame game) {
        return !depleted;
    }
    
    public DuplicatingCube duplicate(){
        return new DuplicatingCube(this);
    }
}
