/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Card;
import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import static com.visitor.game.Game.Zone.VOID;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

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
                && game.hasValidTargetsIn(controller, Predicates::isItem, 5, SCRAPYARD)
                && game.hasValidTargetsIn(controller, Predicates::isItem, 1, VOID)
                && game.hasValidTargetsIn(controller, Predicates::isItem, 1, PLAY);
    }
    
    @Override
    protected void beforePlay(Game game) {
        targets = game.selectFromZone(controller, SCRAPYARD, Predicates::isItem, 5, false);
        Arraylist<UUID> sel = game.selectFromZone(controller, VOID, Predicates::isItem, 1, false);
        targets.addAll(sel);
        Arraylist<UUID> sel2 = game.selectFromZone(controller, PLAY, Predicates::isItem, 1, false);
        targets.addAll(sel2);
        
        
    }
    
    @Override
    protected void duringResolve (Game game){
        if(targets.subList(0, 3).parallelStream().allMatch(i -> { return game.isIn(controller, i, SCRAPYARD);})
            && game.isIn(controller, targets.get(3), VOID)
            && game.isIn(controller, targets.get(4), PLAY)){
            try {
                Arraylist<Card> fromScrap = game.extractAll(targets.subList(0, 3));
                game.putTo(controller, fromScrap, VOID);
                Card voidCard = game.getCard(targets.get(3));
                Card playCard = game.getCard(targets.get(4));
                Card newCard = voidCard.getClass().getDeclaredConstructor(String.class).newInstance(playCard.controller);
                newCard.copyPropertiesFrom(playCard);
                game.transformTo(this, playCard, newCard);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                getLogger(RecombinantReplication.class.getName()).log(SEVERE, null, ex);
            }
        }
    }    
}
