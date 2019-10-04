
package com.visitor.card.types;

import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;

/**
 * Abstract class for the Asset card type.
 * @author pseudo
 */
public abstract class Unit extends Card {
    
    int attack;
    boolean attacking;
    boolean blocking;
    boolean deploying;
    Unit blockedUnit;
        
    public Unit(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, int attack, int health, String owner) {
        super(name, cost, knowledge, text, owner);
        this.attack = attack;
        this.health = health;
    }
    
    @Override
    protected final void duringResolve(Game game) {
        deploying = true;
        game.putTo(controller, this, PLAY);
    }
    
    /* TODO: create a class Activatible unit and move this there
    protected boolean canActivateAdditional(Game game){return true;};
    @Override
    public final boolean canActivate(Game game) {
        return !depleted && !deploying && canActivateAdditional(game);
    }
    */
    
    protected boolean canAttackAdditional(Game game){return true;};
    public final boolean canAttack(Game game) {
        return !depleted && !deploying && canAttackAdditional(game);
    }
    
    protected boolean canBlockAdditional(Game game, Unit u){return true;};
    public final boolean canBlock(Game game, Unit u) {
        return !depleted && canBlockAdditional(game, u);
    }
    
    public final void setAttacking() {
       depleted = true;
       attacking = true;
    }
    
    public final void unsetAttacking() {
       attacking = false;
    }
    
    public final void setBlocking(Unit u) {
        blocking = true;
        blockedUnit = u;
    }
    
    @Override
    public void ready(){
        depleted = false;
        deploying = false;
    }
    
    public final void dealAttackDamage(Game game){
        game.dealDamage(id, blockedUnit.id, attack);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return game.hasEnergy(controller, cost)
               && game.hasKnowledge(controller, knowledge)
               && game.canPlaySlow(controller);
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage()
                .setType("Unit")
                .setAttack(attack);
    }
}
