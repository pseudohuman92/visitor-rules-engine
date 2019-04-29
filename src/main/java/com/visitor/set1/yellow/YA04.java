/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1.yellow;

import com.visitor.card.types.Card;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import com.visitor.protocol.Types;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class YA04 extends Spell {

    int x; 
    
    public YA04(String owner) {
        super("YA04", 0, new Hashmap(YELLOW, 2), 
                "Recover X. \n" +
                "Purge ~.", owner);
    }
    
    @Override
    public boolean canPlay (Game game){
        return super.canPlay(game) 
                && game.hasEnergy(controller, 2)
                && game.hasCardsIn(controller, "scrapyard", 1);
    }
    
    @Override
    public void play (Game game){
        x = game.selectX(controller, Math.min(game.getPlayer(controller).energy/2, 
                                              game.getZone(controller, "scrapyard").size()));
        game.spendEnergy(controller, 2 * x);
        text = "Recover "+x+". \n" +
                "Purge ~.";
        game.addToStack(this);
    } 
    
    @Override
    public void resolveEffect (Game game){}
    
    @Override
    public void resolve (Game game){
        Arraylist<UUID> selected = game.selectFromZone(controller, "scrapyard", c->{return true;}, x, false);
        Arraylist<Card> cards = game.extractAll(selected);
        game.shuffleIntoDeck(controller, cards);
        text = "Recover X. \n" +
                "Purge ~.";
        game.putTo(controller, this, "void");
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage().setCost("XX");
    }
}
