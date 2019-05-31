/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.card.types.Card;
import com.visitor.card.types.Item;
import com.visitor.game.Game;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import com.visitor.helpers.Hashmap;
import java.lang.reflect.InvocationTargetException;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Predicates;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pseudo
 */
public class RecombinantReplication extends Spell {

    
    public RecombinantReplication(String owner) {
        super("Recombinant Replication", 1, new Hashmap(BLUE, 2), 
                "Additional Cost - Purge 3 items from your scrapyard. Transform target item you control into a copy of target item in your void.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) 
                && game.hasValidTargetsIn(controller, Predicates::isItem, 5, "scrapyard")
                && game.hasValidTargetsIn(controller, Predicates::isItem, 1, "void")
                && game.hasValidTargetsIn(controller, Predicates::isItem, 1, "play");
    }
    
    @Override
    public void play(Game game) {
        targets = game.selectFromZone(controller, "scrapyard", Predicates::isItem, 5, false);
        Arraylist<UUID> sel = game.selectFromZone(controller, "void", Predicates::isItem, 1, false);
        targets.addAll(sel);
        Arraylist<UUID> sel2 = game.selectFromZone(controller, "play", Predicates::isItem, 1, false);
        targets.addAll(sel2);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolveEffect (Game game){
        if(targets.subList(0, 3).parallelStream().allMatch(i -> { return game.isIn(controller, i, "scrapyard");})
            && game.isIn(controller, targets.get(3), "void")
            && game.isIn(controller, targets.get(4), "play")){
            try {
                Arraylist<Card> fromScrap = game.extractAll(targets.subList(0, 3));
                game.putTo(controller, fromScrap, "void");
                Card voidCard = game.getCard(targets.get(3));
                Card playCard = game.getCard(targets.get(4));
                Card newCard = voidCard.getClass().getDeclaredConstructor(String.class).newInstance(playCard.controller);
                newCard.copyPropertiesFrom(playCard);
                game.replaceWith(playCard, newCard);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(RecombinantReplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}
