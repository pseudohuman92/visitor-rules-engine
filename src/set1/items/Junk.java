/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items;

import game.ClientGame;
import cards.Item;
import helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public class Junk extends Item{
    
    public Junk (String owner){
        super("Junk", 0, new Hashmap<>(), 
                "Junk can't be played as an item or a source.", 
                "item.png", owner);
    }

    @Override
    public boolean canPlay(ClientGame game) { return false; }
    
    @Override
    public boolean canPlayAsASource(ClientGame game) { return false; }
     
}
