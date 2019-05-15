/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1.blue;

import com.visitor.card.types.Card;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Arraylist;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class UA01 extends Spell {

    UUID target; 
    
    public UA01(String owner) {
        super("UA01", 1, new Hashmap(BLUE, 2), "Purge all copies of a target card from your scrapyard. Deal that many damage.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) && game.hasCardsIn(controller, "scrapyard", 1);
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, "scrapyard", c->{return true;}, 1, false);
        target = targets.get(0);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolveEffect (Game game){
        if(game.isIn(controller, target, "scrapyard")){
            Card c = game.getCard(target);
            Arraylist<Card> cards = game.extractAllCopiesFrom(controller, c.name, "scrapyard");
            game.putTo(controller, cards, "void");
            UUID target = game.selectDamageTargets(controller, 1, false).get(0);
            game.dealDamage(target, cards.size());
        }
    }    
}
