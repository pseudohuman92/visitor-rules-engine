/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.set1.blue;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.card.types.Card;
import com.ccg.ancientaliens.card.types.Item;
import com.ccg.ancientaliens.game.Game;
import static com.ccg.ancientaliens.protocol.Types.Knowledge.BLUE;
import helpers.Hashmap;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pseudo
 */
public class UA05 extends Action {

    public UA05(String owner) {
        super("UA01", 1, new Hashmap(BLUE, 2), 
                "Additional Cost - Purge 5 items from your scrapyard. Transform target item you control into a copy of target item in your void.", owner);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return super.canPlay(game) 
                && game.hasValidTargetsIn(controller, c->{return c instanceof Item;}, 5, "scrapyard")
                && game.hasValidTargetsIn(controller, c->{return c instanceof Item;}, 1, "void")
                && game.hasValidTargetsIn(controller, c->{return c instanceof Item;}, 1, "single play");
    }
    
    @Override
    public void play(Game game) {
        supplementaryData = game.getSelectedFromScrapyard(controller, c->{return c instanceof Item;}, 5);
        ArrayList<UUID> sel = game.getSelectedFromVoid(controller, c->{return c instanceof Item;}, 1);
        supplementaryData.addAll(sel);
        ArrayList<UUID> sel2 = game.getSelectedFromPlay(controller, c->{return (c instanceof Item) && (c.controller.equals(controller));}, 1);
        supplementaryData.addAll(sel2);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
    }
    
    @Override
    public void resolve (Game game){
        if(supplementaryData.subList(0, 5).parallelStream().allMatch(i -> { return game.isIn(controller, i, "scrapyard");})
            && game.isIn(controller, supplementaryData.get(5), "void")
            && game.isIn(controller, supplementaryData.get(6), "single play")){
            try {
                ArrayList<Card> fromScrap = game.extractAll(supplementaryData.subList(0, 5));
                game.putAllTo(controller, fromScrap, "void");
                Card voidCard = game.getCard(supplementaryData.get(5));
                Card playCard = game.getCard(supplementaryData.get(6));
                Card newCard = voidCard.getClass().getDeclaredConstructor(String.class).newInstance(playCard.controller);
                newCard.copyPropertiesFrom(playCard);
                game.replaceWith(playCard, newCard);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(UA05.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        game.putTo(controller, this, "scrapyard");
    }    
}
