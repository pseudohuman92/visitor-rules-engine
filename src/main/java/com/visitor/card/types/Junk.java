/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types;

import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

/**
 *
 * @author pseudo
 */
public class Junk extends Card {

    public Junk(String owner){
        super("Junk", 0, new Hashmap<>(), "Junk can't be played or studied.", owner);
    }
    

    @Override
    public boolean canPlay(Game game) { return false; }
    
    @Override
    public boolean canStudy(Game game) { return false; }

    @Override
    protected void duringResolve(Game game) {
        game.putTo(controller, this, SCRAPYARD);
    }    
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Junk");
    }
    
}
