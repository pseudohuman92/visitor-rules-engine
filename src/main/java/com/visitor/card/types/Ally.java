/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types;

import com.visitor.card.properties.Activatable;
import com.visitor.card.properties.Damageable;
import com.visitor.card.properties.Triggering;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

/**
 *
 * @author pseudo
 */
public abstract class Ally extends Card 
        implements Activatable, Triggering, Damageable {
    
    public int favor;
    public Activation favorAbility;
    public int loyalty;
    public int health;

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
    public void resolve(Game game) {
        game.putTo(controller, this, "play");
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
        System.out.println("Ally is checking event");
        if (game.isTurnPlayer(controller) && event.label.equals("Turn Start") && favor > 0){
            System.out.println("Passed the check");
            favor--;
            if (favor == 0){
                ready();
                game.addToStack(favorAbility);
                favorAbility = null;
            }
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
        game.extractCard(id);
        game.putTo(controller, this, "scrapyard");
        game.removeTriggeringCard(this);
    }
    
    @Override
    public int dealDamage(Game game, int damageAmount) {
        int tmp = Math.min(health, damageAmount);
        if (health <= damageAmount){
            health = 0;
            destroy(game);
        } else {
            health -= damageAmount;
        }
        return tmp;
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Ally")
                .setHealth(health)
                .setFavor(favor)
                .setLoyalty(loyalty);
    }
    
}
