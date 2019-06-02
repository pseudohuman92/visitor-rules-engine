/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types;

import com.visitor.card.properties.Activatable;
import com.visitor.card.properties.Triggering;
import com.visitor.game.Event;
import static com.visitor.game.Event.TURN_START;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import static java.lang.Math.max;
import static java.lang.System.out;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public abstract class Ally extends Card 
        implements Activatable, Triggering {
    
    public int favor;
    public Ability favorAbility;
    public int loyalty;

    public Ally(String name, int cost, 
            Hashmap<Types.Knowledge, Integer> knowledge, 
            String text, int health, String owner) {
        super(name, cost, knowledge, text, owner);
        favor = 0;
        loyalty = 0;
        favorAbility = null;
        this.health = health; 
    }
    
    
    @Override
    protected void duringResolve(Game game) {
        game.putTo(controller, this, PLAY);
        game.registerTriggeringCard(controller, this);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return game.hasEnergy(controller, cost)
               && game.hasKnowledge(controller, knowledge)
               && game.canPlaySlow(controller);
    }
    
    @Override
    public void checkEvent(Game game, Event event){
        out.println("Ally is checking event");
        if (game.isTurnPlayer(controller) && event.type.equals(TURN_START) && favor > 0){
            out.println("Passed the check");
            decreaseFavor(game, 1);
        }
    }
    
    public void decreaseFavor(Game game, int count){
        favor = max(0, favor - count);
        if (favor == 0){
            ready();
            game.addToStack(favorAbility);
            favorAbility = null;
        }
    }
    
    @Override
    public void ready(){
        if (favor == 0){
            depleted = false;
        }
    }
    
    @Override
    public boolean canActivate(Game game) {
        return !depleted && game.canPlaySlow(controller);
    }
    
    @Override
    public void destroy(Game game) {
        super.destroy(game);
        game.removeTriggeringCard(this);
    }
    
    @Override
    public void dealDamage(Game game, int damageAmount, UUID source) {
        if (health <= damageAmount){
            health = 0;
            destroy(game);
        } else {
            health -= damageAmount;
        }
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Ally")
                .setFavor(favor)
                .setLoyalty(loyalty);
    }
    
    @Override
    public void copyPropertiesFrom(Card c){
        super.copyPropertiesFrom(c);
        if (c instanceof Ally){
            favor = ((Ally) c).favor;
            loyalty = ((Ally) c).loyalty;
            
        }
    }
    
}
